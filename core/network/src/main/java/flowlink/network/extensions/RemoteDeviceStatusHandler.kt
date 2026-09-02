package FlowLink.network.extensions

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import FlowLink.domain.model.BatteryState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDeviceStatusHandler @Inject constructor() {
    private val _batteryByDevice = MutableStateFlow<Map<String, BatteryState>>(emptyMap())
    val batteryByDevice: StateFlow<Map<String, BatteryState>> = _batteryByDevice.asStateFlow()

    fun updateBattery(deviceId: String, state: BatteryState) {
        _batteryByDevice.update { it + (deviceId to state) }
    }

    fun clearDeviceStatus(deviceId: String) {
        _batteryByDevice.update { it - deviceId }
    }
}
