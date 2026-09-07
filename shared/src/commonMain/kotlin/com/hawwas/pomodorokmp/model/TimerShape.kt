package com.hawwas.pomodorokmp.model

import androidx.compose.ui.graphics.Color

sealed class TimerShape {
    data class Circle(val mainColor: Color, val emptyBarColor: Color) : TimerShape()
    data class RoundedRect(val mainColor: Color, val emptyBarColor: Color) : TimerShape()
    object None : TimerShape()
}
