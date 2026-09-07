package com.hawwas.pomodorokmp.model

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

sealed class ActionButtonTheme {

  data class Solid(
    val startContainerColor: Color,
    val startContentColor: Color,
    val stopContainerColor: Color,
    val stopContentColor: Color,
    val resetContentColor: Color,
    val contentColor: Color
  ) : ActionButtonTheme()

  data class Outlined constructor(
    val startBorderColor: Color,
    val startContentColor: Color,
    val stopBorderColor: Color,
    val stopContentColor: Color,
    val resetContentColor: Color,
    val contentColor: Color
  ) : ActionButtonTheme()

  data class IconOnly(
    val startContainerColor: Color,
    val startContentColor: Color,
    val stopContainerColor: Color,
    val stopContentColor: Color,
    val resetContentColor: Color,
    val contentColor: Color
  ) : ActionButtonTheme()

  data class Gradient(
    val startGradient: Brush,
    val stopGradient: Brush,
    val contentColor: Color,
    val resetContentColor: Color,

  ) : ActionButtonTheme()
}