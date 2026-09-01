package FlowLink.domain.interfaces

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import FlowLink.domain.model.BaseRemoteDevice
import FlowLink.domain.model.DiscoveredDevice
import FlowLink.domain.model.DeviceConnectionEvent
import FlowLink.domain.model.LocalDevice
import FlowLink.domain.model.PairedDevice
import FlowLink.domain.model.PendingDeviceApproval

interface DeviceManager {
    val pairedDevices: StateFlow<List<PairedDevice>>
    val discoveredDevices: StateFlow<Map<String, DiscoveredDevice>>
    val selectedDeviceId: StateFlow<String?>
    val localDevice: LocalDevice
    val localDeviceFlow: StateFlow<LocalDevice?>
    val pendingDeviceApproval: StateFlow<PendingDeviceApproval?>
    val connectionEvents: SharedFlow<DeviceConnectionEvent>

    fun setPendingApproval(approval: PendingDeviceApproval?)
    fun clearPendingApproval(deviceId: String)
    fun selectDevice(deviceId: String)
    
    suspend fun getDevice(deviceId: String): BaseRemoteDevice?

    suspend fun getDiscoveredDevice(deviceId: String): DiscoveredDevice?
    suspend fun getPairedDevice(deviceId: String): PairedDevice?

    suspend fun addOrUpdateDiscoveredDevice(device: DiscoveredDevice)
    suspend fun removeDiscoveredDevice(deviceId: String)
    
    suspend fun addOrUpdatePairedDevice(device: PairedDevice)
    suspend fun removePairedDevice(deviceId: String)
}

