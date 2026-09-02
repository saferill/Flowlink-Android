package FlowLink.clipboard

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import FlowLink.clipboard.extensions.LanguageDetector
import FlowLink.domain.interfaces.DeviceManager
import FlowLink.domain.interfaces.NetworkManager
import FlowLink.domain.interfaces.PreferencesRepository
import javax.inject.Inject

@AndroidEntryPoint
class ClipboardListener : AccessibilityService() {
    @Inject lateinit var networkManager: NetworkManager

    @Inject lateinit var deviceManager: DeviceManager

    @Inject lateinit var preferencesRepository: PreferencesRepository

    private lateinit var clipboardDetector: ClipboardDetection

    private val serviceScope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default + kotlinx.coroutines.SupervisorJob())
    @Volatile
    private var hasActiveDevice = false

    override fun onCreate() {
        super.onCreate()
        clipboardDetector = ClipboardDetection(LanguageDetector.getCopyForLocale(applicationContext))
        serviceScope.launch {
            deviceManager.pairedDevices.collect { devices ->
                hasActiveDevice = devices.any { it.connectionState.isConnected || it.connectionState.isConnecting }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private var currentPackage: CharSequence? = null
    private var runForNextEventAlso = false
    private var lastDetectionTimeMs = 0L
    private val minDetectionInterval = 100 // Minimum time between detections


    override fun onServiceConnected() {
        super.onServiceConnected()
        
        val info = AccessibilityServiceInfo()
        info.apply {
            eventTypes = MONITORED_EVENTS
            feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
            flags = flags or AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
            notificationTimeout = 120
        }
        serviceInfo = info
        
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!hasActiveDevice) return
        
        try {
            if (event?.packageName != packageName)
                currentPackage = event?.packageName

            if (event?.eventType != null)
                clipboardDetector.addEvent(event.eventType)
            val currentTimeMs = System.currentTimeMillis()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                && clipboardDetector.getSupportedEventTypes(event)) {

                if (currentTimeMs - lastDetectionTimeMs < minDetectionInterval) {
                    Log.d(TAG, "Ignoring duplicate detection")
                    return
                }

                lastDetectionTimeMs = currentTimeMs
                runForNextEventAlso = true
                Log.d(TAG,"Running for first time")
                runChangeClipboardActivity()
                return
            }

            if (runForNextEventAlso) {
                Log.d(TAG,"Running for second time")
                runForNextEventAlso = false
                runChangeClipboardActivity()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Accessibility Service Error", e)
        }
    }

    private val lock = Any()
    private fun runChangeClipboardActivity() = synchronized(lock) {
        ClipboardChangeActivity.launch(applicationContext)
    }

    override fun onInterrupt() {}

    companion object {
        private const val TAG = "ClipboardService"

        private const val MONITORED_EVENTS = AccessibilityEvent.TYPE_VIEW_CLICKED or
                AccessibilityEvent.TYPE_VIEW_FOCUSED or
                AccessibilityEvent.TYPE_VIEW_LONG_CLICKED or
                AccessibilityEvent.TYPE_VIEW_SELECTED or
                AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED or
                AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED or
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED or
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
    }
}