package FlowLink.domain.model

sealed class DeviceConnectionEvent {
    data object OnConnectionStatusChanged : DeviceConnectionEvent()
}
