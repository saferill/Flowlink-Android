/*
 * Acknowledgment:
 * Portions of this code are adapted from XClipper by Kaustubh Patange.
 * Licensed under the Apache License 2.0.
 */

package FlowLink.clipboard

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import FlowLink.domain.interfaces.NetworkManager
import FlowLink.domain.model.ClipboardInfo
import javax.inject.Inject

@AndroidEntryPoint
class ClipboardChangeActivity : FragmentActivity() {
    @Inject lateinit var networkManager: NetworkManager

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        lifecycleScope.launch {
            delay(250)
            if (hasFocus) {
                val clip = clipboardManager.primaryClip
                if (clip != null && clip.itemCount > 0) {
                    val text = clip.getItemAt(0)?.text?.toString()
                    if (!text.isNullOrBlank() && 
                        text != "null" && 
                        text != ClipboardHandler.lastReceivedText && 
                        text != lastSentText) {
                        lastSentText = text
                        networkManager.sendClipboardMessage(ClipboardInfo("text/plain", text))
                    }
                }
                finish()
            }
        }
        super.onWindowFocusChanged(hasFocus)
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        onWindowFocusChanged(true)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        @Volatile
        var lastSentText: String? = null

        fun launch(context: Context) = with(context) {
            val intent = Intent(this, ClipboardChangeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}