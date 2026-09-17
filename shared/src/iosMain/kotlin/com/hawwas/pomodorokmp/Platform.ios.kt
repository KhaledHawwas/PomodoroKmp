package com.hawwas.pomodorokmp

import platform.UIKit.UIDevice
import platform.AudioToolbox.AudioServicesPlaySystemSound

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun isMeshGradientSupported(): Boolean = true

actual fun playBeepSound() {
    // Note: To play sonar.wav on iOS, we would use AVAudioPlayer.
    // Keeping system sound for now to maintain build stability.
    AudioServicesPlaySystemSound(1005u)
}
