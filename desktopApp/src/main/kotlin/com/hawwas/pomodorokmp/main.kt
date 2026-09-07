package com.hawwas.pomodorokmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "PomodoroKmp",
    ) {
        App()
    }
}