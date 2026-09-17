package com.hawwas.pomodorokmp

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual fun isMeshGradientSupported(): Boolean = true

actual fun playBeepSound() {
    js("try { var context = new (window.AudioContext || window.webkitAudioContext)(); var oscillator = context.createOscillator(); oscillator.type = 'sine'; oscillator.frequency.value = 800; oscillator.connect(context.destination); oscillator.start(); setTimeout(function(){ oscillator.stop(); }, 200); } catch(e) {}")
}