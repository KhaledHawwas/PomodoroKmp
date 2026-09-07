package com.hawwas.pomodorokmp

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hawwas.pomodorokmp.model.SurroundedTextTheme

@Composable
fun    SurroundingText(theme: SurroundedTextTheme.MovingTextTheme) {
    val infiniteTransition = rememberInfiniteTransition(label = "meshMovement")

    val textRotationProgress by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(theme.duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "textRotation"
    )
    TextOnPath(
        text = theme.text, modifier = Modifier.size(340.dp), style = TextStyle(
            color = Color.White.copy(alpha = 0.6f), fontSize = 16.sp, fontWeight = FontWeight.Medium
        ), progress = textRotationProgress, createPath = { size ->
            Path().apply {
                addOval(Rect(Offset.Zero, size))
            }
        })
}