package com.hawwas.pomodorokmp.model

sealed class SurroundedTextTheme {
    data class MovingTextTheme(val text: String, val duration: Int) : SurroundedTextTheme()
    object None : SurroundedTextTheme()
}
