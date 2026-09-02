package FlowLink.domain.interfaces

import FlowLink.domain.model.ClipboardInfo
import FlowLink.domain.model.ConnectionDetails
import FlowLink.domain.model.PairedDevice
import FlowLink.domain.model.SocketMessage

interface NetworkManager {
    suspend fun connectPaired(device: PairedDevice)
    suspend fun connectTo(connectionDetails: ConnectionDetails)
    suspend fun disconnect(deviceId: String)
    fun broadcastMessage(message: SocketMessage)
    fun sendMessage(deviceId: String, message: SocketMessage)
    fun sendClipboardMessage(message: ClipboardInfo)
    fun getActiveConnectionAddress(deviceId: String): String?
    suspend fun approveDeviceConnection(deviceId: String)
    suspend fun rejectDeviceConnection(deviceId: String)
}