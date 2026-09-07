package com.hawwas.pomodorokmp.model

import androidx.compose.ui.graphics.Color

sealed class TimerTextTheme {
    data class Solid(val color: Color, val showPercentage: Boolean, val animated: Boolean = false) :
        TimerTextTheme()
}
