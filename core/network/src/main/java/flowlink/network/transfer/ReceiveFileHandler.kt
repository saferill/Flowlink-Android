package FlowLink.network.transfer

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import io.ktor.utils.io.cancel
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import io.ktor.utils.io.readAvailable
import io.ktor.utils.io.streams.asByteWriteChannel
import io.ktor.utils.io.writeStringUtf8
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import FlowLink.common.R
import FlowLink.common.util.createTempFileUri
import FlowLink.domain.model.FileMetadata
import FlowLink.domain.interfaces.PreferencesRepository
import FlowLink.network.util.formatSize
import java.io.File
import java.io.IOException
import javax.net.ssl.SSLSocket

/**
 * Handles receiving files from a remote device.
 * @param notifications Optional notification helper. If null, no notifications are shown (e.g., clipboard transfers).
 */
class ReceiveFileHandler(
    private val context: Context,
    private val transferId: String,
    private val clientSocket: SSLSocket,
    private val files: List<FileMetadata>,
    private val deviceName: String,
    private val preferencesRepository: PreferencesRepository? = null,
    private val notifications: TransferNotificationHelper? = null
) {
    val totalBytes: Long = files.sumOf { it.fileSize }
    private var totalBytesReceived: Long = 0
    
    var lastFileUri: Uri? = null
        private set

    private val isSilent: Boolean get() = notifications == null

    suspend fun receive(): Uri? {
        clientSocket.tcpNoDelay = true
        clientSocket.sendBufferSize = 2 * 1024 * 1024
        clientSocket.receiveBufferSize = 2 * 1024 * 1024

        val readChannel = clientSocket.inputStream.toByteReadChannel()
        val writeChannel = clientSocket.outputStream.asByteWriteChannel()

        try {
            notifications?.let {
                val title = context.getString(
                    R.string.notification_receiving_title_format,
                    context.getString(R.string.notification_receiving_action),
                    files.size,
                    if (files.size == 1) {
                        context.getString(R.string.notification_file)
                    } else {
                        context.getString(R.string.notification_files)
                    },
                    context.getString(R.string.notification_from),
                    deviceName
                )
                
                it.showProgress(transferId = transferId, title = title)
            }

            files.forEachIndexed { index, metadata ->
                currentCoroutineContext().ensureActive()

                lastFileUri = receiveFile(readChannel, writeChannel, metadata, index + 1)
            }

            notifications?.showCompleted(
                transferId,
                files.size,
                fileUri = if (files.size == 1) lastFileUri else null,
                mimeType = if (files.size == 1) files.first().mimeType else null
            )

            return lastFileUri
        } catch (e: Exception) {
            Log.e(TAG, "Receive failed", e)
            if (e !is kotlinx.coroutines.CancellationException) {
                notifications?.showError(transferId, e.message ?: "Transfer failed")
            }
            throw e
        } finally {
            readChannel.cancel()
            writeChannel.flushAndClose()
            try { clientSocket.close() } catch (_: Exception) { }
        }
    }

    private suspend fun receiveFile(
        readChannel: io.ktor.utils.io.ByteReadChannel,
        writeChannel: io.ktor.utils.io.ByteWriteChannel,
        metadata: FileMetadata,
        fileIndex: Int
    ): Uri {
        val fileUri = createOutputUri(metadata)
        
        try {
            // Send "start" message to indicate we're ready to receive this file
            writeChannel.writeStringUtf8("start\n")
            writeChannel.flush()
            
            context.contentResolver.openOutputStream(fileUri)?.use { output ->
                var currentFileReceived = 0L
                var lastProgressUpdateTime = 0L
                val adaptiveBufferSize = getAdaptiveBufferSize(metadata.fileSize)
                val buffer = ByteArray(adaptiveBufferSize)

                while (currentFileReceived < metadata.fileSize) {
                    currentCoroutineContext().ensureActive()

                    val bytesToRead = minOf(buffer.size.toLong(), metadata.fileSize - currentFileReceived).toInt()
                    val bytesRead = readChannel.readAvailable(buffer, 0, bytesToRead)

                    if (bytesRead <= 0) break
                    output.write(buffer, 0, bytesRead)
                    currentFileReceived += bytesRead
                    totalBytesReceived += bytesRead

                    val now = System.currentTimeMillis()
                    if (now - lastProgressUpdateTime > 150 || totalBytesReceived >= totalBytes) {
                        lastProgressUpdateTime = now
                        notifications?.let {
                            val progress = ((totalBytesReceived.toFloat() / totalBytes) * 100).toInt()
                            val title = context.getString(
                                R.string.notification_receiving_title_format,
                                context.getString(R.string.notification_receiving_action),
                                files.size,
                                if (files.size == 1) {
                                    context.getString(R.string.notification_file)
                                } else {
                                    context.getString(R.string.notification_files)
                                },
                                context.getString(R.string.notification_from),
                                deviceName
                            )
                            
                            val fileInfo = if (files.size > 1) {
                                "${metadata.fileName} ($fileIndex/${files.size})"
                            } else {
                                metadata.fileName
                            }
                            
                            val progressText = context.getString(
                                R.string.notification_progress_format,
                                progress,
                                formatSize(totalBytesReceived),
                                formatSize(totalBytes)
                            )

                            it.updateProgress(
                                transferId = transferId,
                                title = title,
                                subText = progressText,
                                contentText = fileInfo,
                                progress = progress
                            )
                        }
                    }
                }

                if (currentFileReceived != metadata.fileSize) {
                    throw IOException("Incomplete transfer: received $currentFileReceived bytes out of ${metadata.fileSize}")
                }

                writeChannel.writeStringUtf8("${SendFileHandler.TRANSFER_COMPLETE_MESSAGE}\n")
                writeChannel.flush()
            } ?: throw IOException("Failed to open output stream")
            
            return fileUri
        } catch (e: Exception) {
            try { context.contentResolver.delete(fileUri, null, null) }
            catch (_: Exception) { }
            throw e
        }
    }

    private suspend fun createOutputUri(metadata: FileMetadata): Uri {
        return if (isSilent) {
            val extension = metadata.fileName.substringAfterLast('.', "")
            val prefix = metadata.fileName.substringBeforeLast('.')
            createTempFileUri(context, prefix.ifEmpty { "temp" }, extension)
        } else {
            val uniqueName = getUniqueFileName(metadata.fileName)
            
            if (preferencesRepository?.getStorageLocation()?.first()?.isNotEmpty() == true) {
                val storageUri = preferencesRepository.getStorageLocation().first().toUri()
                val directory = DocumentFile.fromTreeUri(context, storageUri)
                    ?: throw IOException("Failed to access custom storage")
                directory.createFile(metadata.mimeType, uniqueName)?.uri
                    ?: throw IOException("Failed to create file in custom storage")
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, uniqueName)
                    put(MediaStore.Downloads.MIME_TYPE, metadata.mimeType)
                    put(MediaStore.Downloads.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/FlowLink/")
                }
                context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    ?: throw IOException("Failed to create MediaStore entry for $uniqueName")
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val targetDir = File(downloadsDir, "FlowLink").apply { mkdirs() }
                File(targetDir, uniqueName).toUri()
            }
        }
    }

    private fun getUniqueFileName(fileName: String): String {
        val extension = fileName.substringAfterLast(".", "")
            .let { if (it.isNotEmpty()) ".$it" else "" }
        val baseName = fileName.substringBeforeLast(".")

        fun exists(name: String): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                downloadFileExists(name)
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                File(File(downloadsDir, "FlowLink"), name).exists()
            }
        }

        if (!exists(fileName)) {
            return fileName
        }

        var copyIndex = 1
        var candidate: String
        do {
            candidate = "$baseName ($copyIndex)$extension"
            copyIndex++
        } while (exists(candidate))

        return candidate
    }

    private fun downloadFileExists(fileName: String): Boolean {
        val projection = arrayOf(MediaStore.Downloads._ID)
        val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ? AND ${MediaStore.Downloads.RELATIVE_PATH} = ?"
        val selectionArgs = arrayOf(fileName, "${Environment.DIRECTORY_DOWNLOADS}/FlowLink/")

        return context.contentResolver.query(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            cursor.moveToFirst()
        } ?: false
    }

    companion object {
        private const val TAG = "ReceiveFileHandler"

        fun getAdaptiveBufferSize(fileSize: Long): Int {
            return when {
                fileSize <= 1024 * 1024 -> 64 * 1024        // 64 KB (Instant for photos/docs)
                fileSize <= 20 * 1024 * 1024 -> 256 * 1024   // 256 KB (Fast for medium files)
                fileSize <= 100 * 1024 * 1024 -> 1024 * 1024 // 1 MB (High throughput)
                else -> 4 * 1024 * 1024                      // 4 MB (Wire-speed for large 4K video)
            }
        }
    }
}
