package FlowLink.common.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.net.URLDecoder
import androidx.core.net.toUri

fun createTempFileUri(context: Context, prefix: String, extension: String): Uri {
    val tempFile = createTempFile(context, prefix, extension)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempFile)
}

fun createTempFile(context: Context, prefix: String, extension: String): File {
    return File.createTempFile(
        prefix,
        if (extension.isNotEmpty()) ".$extension" else "",
        context.cacheDir
    ).apply { deleteOnExit() }
}

fun getFileProviderUri(context: Context, file: File): Uri {
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

fun getReadablePathFromUri(context: Context, uriString: String): String {
    return if (uriString.startsWith("content://")) {
        // Parse the URI and convert it to a human-readable path
        val uri = uriString.toUri()
        getPathFromTreeUri(uri)
    } else {
        // Return the file path as is (e.g., "/storage/emulated/0/Downloads")
        "/storage/emulated/0/Download"
    }
}

private fun getPathFromTreeUri(uri: Uri): String {
    // Decode the URI to make it human-readable
    val decodedPath = URLDecoder.decode(uri.toString(), "UTF-8")

    return when {
        decodedPath.contains("primary:") -> {
            // Convert "primary:" to "/storage/emulated/0/" for primary storage
            decodedPath.replaceFirst("content://com.android.externalstorage.documents/tree/primary:", "/storage/emulated/0/")
                .replaceFirst("/document/primary:", "")
        }
        else -> {
            // Fallback if it's not the primary storage
            decodedPath
        }
    }
}