package com.hawwas.pomodorokmp

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import com.hawwas.pomodorokmp.model.BackgroundTheme

@Composable
fun MeshGradientBackground(theme: BackgroundTheme.Mesh) {
    val duration = theme.animationDuration
    val animatedOffset = if (duration != null) {
        val infiniteTransition = rememberInfiniteTransition(label = "meshMovement")
        infiniteTransition.animateFloat(
            initialValue = -0.1f, targetValue = 0.1f, animationSpec = infiniteRepeatable(
                animation = tween(duration, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "offset"
        ).value
    } else {
        0f
    }


    val coral = Color(255, 90, 90)
    val peach = Color(255, 139, 90)
    val amber = Color(255, 169, 90)
    val sunshine = Color(255, 212, 90)
    val indigo = Color(0xFF5856D6)
    val pink = Color(0xFFFF2D55)


    val gradientPainter = remember(animatedOffset) {
        theme.meshPainter(animatedOffset)
    }
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White).paint(gradientPainter)
    )
}