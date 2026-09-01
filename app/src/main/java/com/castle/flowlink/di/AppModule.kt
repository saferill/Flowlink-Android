package com.castle.FlowLink.di

import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.komu.sekia.di.AppCoroutineScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import FlowLink.domain.interfaces.DeviceManager
import FlowLink.domain.interfaces.NetworkManager
import FlowLink.domain.interfaces.NotificationCallback
import FlowLink.domain.interfaces.PreferencesRepository
import FlowLink.domain.interfaces.SocketFactory
import FlowLink.data.repository.DeviceManagerImpl
import FlowLink.data.repository.PreferencesRepositoryImpl
import FlowLink.network.NetworkManagerImpl
import FlowLink.network.SocketFactoryImpl
import FlowLink.notification.NotificationService
import FlowLink.projection.media.PlaybackService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AppModule {

    @Binds
    abstract fun bindSocketFactory(impl: SocketFactoryImpl): SocketFactory

    @Binds
    abstract fun bindDeviceManager(impl: DeviceManagerImpl): DeviceManager

    @Binds
    abstract fun bindNetworkManager(impl: NetworkManagerImpl): NetworkManager

    @Binds
    abstract fun bindPreferencesRepository(impl: PreferencesRepositoryImpl): PreferencesRepository

    companion object {
        @Provides
        fun provideAppContext(@ApplicationContext context: Context) = context

        @Provides
        @Singleton
        fun provideAppCoroutineScope(): AppCoroutineScope {
            return object : AppCoroutineScope {
                override val coroutineContext =
                    SupervisorJob() + Dispatchers.Main.immediate + CoroutineName("App")
            }
        }

        @Provides
        @Singleton
        fun provideNotificationCallback(
            notificationService: NotificationService,
            playbackService: PlaybackService
        ): NotificationCallback = object : NotificationCallback {
            override fun onNotificationPosted(notification: StatusBarNotification) {
                notificationService.onNotificationPosted(notification)
                playbackService.onNotificationPosted(notification)
            }

            override fun onNotificationRemoved(notification: StatusBarNotification) {
                notificationService.onNotificationRemoved(notification)
                playbackService.onNotificationRemoved(notification)
            }

            override fun onListenerConnected(service: NotificationListenerService) {
                notificationService.onListenerConnected(service)
                playbackService.onListenerConnected(service)
            }

            override fun onListenerDisconnected() {
                notificationService.onListenerDisconnected()
                playbackService.onListenerDisconnected()
            }
        }
    }
}