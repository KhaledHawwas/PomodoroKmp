package com.hawwas.pomodorokmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun isMeshGradientSupported(): Boolean

expect fun playBeepSound()