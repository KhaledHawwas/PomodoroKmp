package com.hawwas.pomodorokmp

import web.navigator.navigator

class JsPlatform: Platform {
    private val userAgent = navigator.userAgent
    private val browserList = listOf("Chrome", "Firefox", "Safari", "Edge")

    override val name: String = userAgent.findAnyOf(browserList, ignoreCase = true)
            ?.let { (startIndex) -> userAgent.substring(startIndex).substringBefore(" ") }
            ?: "Unknown"
}

actual fun getPlatform(): Platform = JsPlatform()

actual fun isMeshGradientSupported(): Boolean = true

actual fun playBeepSound() {
    js("try { var audio = new Audio('composeResources/pomodorokmp.shared.generated.resources/files/sonar.wav'); audio.play(); } catch(e) { console.error('Audio play failed', e); }")
}
