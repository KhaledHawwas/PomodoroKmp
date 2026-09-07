package com.hawwas.pomodorokmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun Brush.Companion.stripes(
    stripes: List<Pair<Color, Float>>,
    width: Float = 20f,
    angle: Float = 45f,
    origin: Offset = Offset.Zero,
): Brush {
    val totalWeight = stripes.sumOf { it.second.toDouble() }.toFloat()

    val colorStops = mutableListOf<Pair<Float, Color>>()
    var currentPosition = 0f
    stripes.forEach { (color, weight) ->
        val proportion = weight / totalWeight
        colorStops.add(currentPosition to color)
        currentPosition += proportion
        colorStops.add(currentPosition to color)
    }

    val angleInRadians = angle * (PI / 180)
    val dir = Offset(cos(angleInRadians).toFloat(), sin(angleInRadians).toFloat())

    return linearGradient(
        colorStops = colorStops.toTypedArray(),
        start = origin,
        end = origin + dir * width,
        tileMode = TileMode.Repeated,
    )
}

@Preview
@Composable
fun stripesPrev(){
    Box(modifier =
        Modifier.fillMaxSize().drawBehind {
            drawRect(brush = Brush.stripes(stripes = listOf(
                Color(0xffc76b98) to 1f,
                Color(0xfff09f9c) to 1f,
                Color(0xFFfcc3a3) to 1f,
            ), width = 245f, angle = 45f))
        }
    )
}