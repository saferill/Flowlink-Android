package FlowLink.network.transfer

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import FlowLink.domain.model.FileTransferInfo
import FlowLink.clipboard.ClipboardHandler
import FlowLink.domain.interfaces.DeviceManager
import FlowLink.domain.model.ServerInfo
import FlowLink.domain.interfaces.NetworkManager
import FlowLink.domain.interfaces.PreferencesRepository
import FlowLink.domain.interfaces.SocketFactory
import FlowLink.network.util.getFileMetadata
import java.io.IOException
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileTransferService @Inject constructor(
    private val context: Context,
    private val socketFactory: SocketFactory,
    private val deviceManager: DeviceManager,
    private val preferencesRepository: PreferencesRepository,
    private val notifications: TransferNotificationHelper,
    private val networkManager: NetworkManager,
    private val clipboardHandler: ClipboardHandler
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val activeTransfers = ConcurrentHashMap<String, Job>()

    fun sendFiles(deviceId: String, fileUris: List<Uri>) {
        val transferId = UUID.randomUUID().toString()

        val job = scope.launch {
            try {
                val device = deviceManager.getPairedDevice(deviceId)
                    ?: throw IOException("Device $deviceId not found")
                
                val filesMetadata = fileUris.map { getFileMetadata(context, it) }
                
                val serverSocket = socketFactory.tcpServerSocket(PORT_RANGE, device.certificate)
                    ?: throw IOException("Failed to create server socket")

                val serverInfo = ServerInfo(serverSocket.localPort)

                val handler = SendFileHandler(
                    context = context,
                    transferId = transferId,
                    serverSocket = serverSocket,
                    fileUris = fileUris,
                    filesMetadata = filesMetadata,
                    deviceName = device.deviceName,
                    notifications = notifications
                )

                networkManager.sendMessage(deviceId, FileTransferInfo(files = filesMetadata, serverInfo = serverInfo))
                handler.send()
            } catch (e: CancellationException) {
                Log.d(TAG, "Transfer $transferId cancelled")
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Send files failed", e)
            } finally {
                activeTransfers.remove(transferId)
            }
        }
        activeTransfers[transferId] = job
    }

    fun receiveFiles(deviceId: String, transfer: FileTransferInfo) {
        val transferId = UUID.randomUUID().toString()

        val job = scope.launch {
            try {
                val device = deviceManager.getPairedDevice(deviceId)
                    ?: throw IOException("Device $deviceId not found")

                val candidateAddresses = buildList {
                    networkManager.getActiveConnectionAddress(deviceId)?.let { add(it) }
                    device.address?.let { if (!contains(it)) add(it) }
                    device.getAddressesToTry().forEach { if (!contains(it)) add(it) }
                }

                if (candidateAddresses.isEmpty()) {
                    throw IOException("No connected address for device $deviceId")
                }

                var clientSocket: javax.net.ssl.SSLSocket? = null
                for (addr in candidateAddresses) {
                    try {
                        Log.d(TAG, "Attempting file transfer connection to $addr:${transfer.serverInfo.port}")
                        clientSocket = socketFactory.tcpClientSocket(addr, transfer.serverInfo.port, device.certificate)
                        if (clientSocket != null) {
                            Log.d(TAG, "Connected to file transfer server at $addr:${transfer.serverInfo.port}")
                            break
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "Failed connection attempt to $addr:${transfer.serverInfo.port}: ${e.message}")
                    }
                }

                val connectedSocket = clientSocket 
                    ?: throw IOException("Failed to establish connection to any candidate address: $candidateAddresses")

                val handler = ReceiveFileHandler(
                    context = context,
                    transferId = transferId,
                    clientSocket = connectedSocket,
                    files = transfer.files,
                    deviceName = device.deviceName,
                    preferencesRepository = if (transfer.isClipboard) null else preferencesRepository,
                    notifications = if (transfer.isClipboard) null else notifications
                )

                val fileUri = handler.receive()
                fileUri?.let { clipboardHandler.setClipboardUri(it) }
            } catch (e: CancellationException) {
                Log.d(TAG, "Transfer $transferId cancelled")
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Receive files failed", e)
            } finally {
                activeTransfers.remove(transferId)
            }
        }
        activeTransfers[transferId] = job
    }

    fun cancelTransfer(transferId: String) {
        activeTransfers[transferId]?.cancel()
        notifications.cancel(transferId)
        activeTransfers.remove(transferId)
    }

    companion object {
        private const val TAG = "FileTransferManager"
        val PORT_RANGE = 5152..5169
    }
}
