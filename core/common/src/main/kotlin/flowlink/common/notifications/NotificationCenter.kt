package FlowLink.common.notifications

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationChannelGroupCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import FlowLink.common.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationCenter @Inject constructor(
    private val context: Context
) {
    private val manager = NotificationManagerCompat.from(context)

    @SuppressLint("MissingPermission")
    fun showNotification(
        channelId: String,
        notificationId: Int,
        builder: NotificationCompat.Builder.() -> Unit = {}
    ) : NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            priority = NotificationCompat.PRIORITY_DEFAULT
            builder(this)
            manager.notify(notificationId, build())
        }
    }

    fun cancelNotification(notificationId: Int) = manager.cancel(notificationId)

    @SuppressLint("MissingPermission")
    fun modifyNotification(
        builder: NotificationCompat.Builder,
        notificationId: Int,
        modifierBlock: NotificationCompat.Builder.() -> Unit
    ) {
        modifierBlock(builder)
        manager.notify(notificationId, builder.build())
    }
}
fun buildNotificationChannelGroup(
    channelId: String,
    block: (NotificationChannelGroupCompat.Builder.() -> Unit),
): NotificationChannelGroupCompat {
    val builder = NotificationChannelGroupCompat.Builder(channelId)
    builder.block()
    return builder.build()
}

fun buildNotificationChannel(
    channelId: String,
    channelImportance: Int,
    block: (NotificationChannelCompat.Builder.() -> Unit),
): NotificationChannelCompat {
    val builder = NotificationChannelCompat.Builder(channelId, channelImportance)
    builder.block()
    return builder.build()
}

