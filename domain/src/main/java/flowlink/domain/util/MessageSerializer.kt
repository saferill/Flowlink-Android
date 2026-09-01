package FlowLink.domain.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import FlowLink.domain.model.ActionInfo
import FlowLink.domain.model.ApplicationInfo
import FlowLink.domain.model.ApplicationList
import FlowLink.domain.model.AudioDeviceInfo
import FlowLink.domain.model.AudioStreamState
import FlowLink.domain.model.Authentication
import FlowLink.domain.model.BatteryState
import FlowLink.domain.model.BluetoothPairingRequest
import FlowLink.domain.model.BluetoothPairingResult
import FlowLink.domain.model.CallInfo
import FlowLink.domain.model.CallLogInfo
import FlowLink.domain.model.ClearNotifications
import FlowLink.domain.model.ClipboardInfo
import FlowLink.domain.model.ConnectionAck
import FlowLink.domain.model.ContactInfo
import FlowLink.domain.model.ConversationInfo
import FlowLink.domain.model.DeviceInfo
import FlowLink.domain.model.Disconnect
import FlowLink.domain.model.DndState
import FlowLink.domain.model.FileTransferInfo
import FlowLink.domain.model.MediaAction
import FlowLink.domain.model.NotificationAction
import FlowLink.domain.model.NotificationInfo
import FlowLink.domain.model.NotificationReply
import FlowLink.domain.model.PairMessage
import FlowLink.domain.model.PlaybackInfo
import FlowLink.domain.model.RequestApplicationList
import FlowLink.domain.model.RingerModeState
import FlowLink.domain.model.SftpServerInfo
import FlowLink.domain.model.SocketMessage
import FlowLink.domain.model.TextMessage
import FlowLink.domain.model.ThreadRequest
import FlowLink.domain.model.UdpBroadcast

object MessageSerializer {
    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        serializersModule = SerializersModule {
            polymorphic(SocketMessage::class) {
                subclass(ActionInfo::class)
                subclass(ApplicationInfo::class)
                subclass(ApplicationList::class)
                subclass(Authentication::class)
                subclass(AudioDeviceInfo::class)
                subclass(AudioStreamState::class)
                subclass(BatteryState::class)
                subclass(BluetoothPairingResult::class)
                subclass(BluetoothPairingRequest::class)
                subclass(CallInfo::class)
                subclass(CallLogInfo::class)
                subclass(ClearNotifications::class)
                subclass(ClipboardInfo::class)
                subclass(ConnectionAck::class)
                subclass(ContactInfo::class)
                subclass(ConversationInfo::class)
                subclass(DeviceInfo::class)
                subclass(Disconnect::class)
                subclass(DndState::class)
                subclass(FileTransferInfo::class)
                subclass(MediaAction::class)
                subclass(NotificationAction::class)
                subclass(NotificationInfo::class)
                subclass(NotificationReply::class)
                subclass(PairMessage::class)
                subclass(PlaybackInfo::class)
                subclass(RequestApplicationList::class)
                subclass(RingerModeState::class)
                subclass(SftpServerInfo::class)
                subclass(TextMessage::class)
                subclass(ThreadRequest::class)
                subclass(UdpBroadcast::class)
            }
        }
        isLenient = true
        decodeEnumsCaseInsensitive = true
        classDiscriminator = "type"
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun serialize(message: SocketMessage): String? {
        return runCatching {
             json.encodeToString(SocketMessage.serializer(), message)
        }.getOrNull()
    }

    fun deserialize(jsonString: String): SocketMessage? {
        return runCatching {
            json.decodeFromString<SocketMessage>(jsonString)
        }.getOrNull()
    }
}
