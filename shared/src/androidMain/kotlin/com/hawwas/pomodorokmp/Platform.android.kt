package com.hawwas.pomodorokmp

import android.os.Build
import android.media.MediaPlayer
import android.content.Context

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun isMeshGradientSupported(): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

private var appContext: Context? = null

fun setAppContext(context: Context) {
    appContext = context.applicationContext
}

actual fun playBeepSound() {
    val context = appContext
    if (context != null) {
        try {
            val mediaPlayer = MediaPlayer()
            val assetDescriptor = context.assets.openFd("composeResources/pomodorokmp.shared.generated.resources/files/sonar.wav")
            mediaPlayer.setDataSource(assetDescriptor.fileDescriptor, assetDescriptor.startOffset, assetDescriptor.length)
            assetDescriptor.close()
            mediaPlayer.prepare()
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { it.release() }
            return
        } catch (e: Exception) {
            // Fallback to tone if file fails
        }
    }

    try {
        val toneGen = android.media.ToneGenerator(android.media.AudioManager.STREAM_ALARM, 100)
        toneGen.startTone(android.media.ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
    } catch (e: Exception) {
        // Ignore
    }
}
