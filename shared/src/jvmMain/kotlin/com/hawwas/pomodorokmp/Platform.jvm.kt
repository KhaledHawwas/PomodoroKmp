package com.hawwas.pomodorokmp

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun isMeshGradientSupported(): Boolean = true

actual fun playBeepSound() {
    try {
        val resourcePath = "composeResources/pomodorokmp.shared.generated.resources/files/sonar.wav"
        val inputStream = JVMPlatform::class.java.classLoader.getResourceAsStream(resourcePath)
        if (inputStream != null) {
            val audioStream = javax.sound.sampled.AudioSystem.getAudioInputStream(java.io.BufferedInputStream(inputStream))
            val clip = javax.sound.sampled.AudioSystem.getClip()
            clip.open(audioStream)
            clip.start()
            clip.addLineListener { event ->
                if (event.type == javax.sound.sampled.LineEvent.Type.STOP) {
                    clip.close()
                }
            }
        } else {
            java.awt.Toolkit.getDefaultToolkit().beep()
        }
    } catch (e: Exception) {
        java.awt.Toolkit.getDefaultToolkit().beep()
    }
}