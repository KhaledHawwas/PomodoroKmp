package com.hawwas.pomodorokmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
fun main() = application {
    val windowState = rememberWindowState(
        placement = WindowPlacement.Floating,
        size = DpSize(900.dp, 600.dp)
    )
    var isFullscreen by remember { mutableStateOf(false) }

    key(isFullscreen) {
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Pomodoro",
            transparent = isFullscreen,
            undecorated = isFullscreen,
            onKeyEvent = { event ->
                if (event.key == Key.Escape && isFullscreen) {
                    isFullscreen = false
                    windowState.placement = WindowPlacement.Floating
                    true
                } else {
                    false
                }
            }
        ) {
            App(
                isFullscreen = isFullscreen,
                onToggleFullscreen = {
                    isFullscreen = !isFullscreen
                    windowState.placement =
                        if (isFullscreen) WindowPlacement.Maximized else WindowPlacement.Floating
                }
            )
        }
    }
}