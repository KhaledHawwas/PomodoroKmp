package com.hawwas.pomodorokmp

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import com.hawwas.pomodorokmp.model.BackgroundTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ArrowStrokeBackground(
    theme: BackgroundTheme.ArrowStroke,
    modifier: Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scroll = if (theme.animationDuration != null) {
        infiniteTransition.animateFloat(
            initialValue = theme.width,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(18000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        ).value
    } else 0f

    Box(
        modifier.drawBehind {
            val seamX = size.width / 2f
            val angle1Rad = (theme.angle * PI / 180).toFloat()
            val dir1 = Offset(cos(angle1Rad), sin(angle1Rad))
            val angle2Rad = ((180f - theme.angle) * PI / 180).toFloat()
            val dir2 = Offset(cos(angle2Rad), sin(angle2Rad))

            clipRect(left = 0f, right = seamX) {
                drawRect(
                    brush = Brush.stripes(
                        stripes = theme.stripes, width = theme.width, angle = theme.angle,
                        origin = dir1 * scroll
                    )
                )
            }
            clipRect(left = seamX, right = size.width) {
                drawRect(
                    brush = Brush.stripes(
                        stripes = theme.stripes, width = theme.width, angle = 180f - theme.angle,
                        origin = Offset(size.width, 0f) + dir2 * scroll
                    )
                )
            }
        }
    )
}

@Composable
@Preview
fun ArrowStrockBackgroundPrev() {
    ArrowStrokeBackground(
        modifier = Modifier.fillMaxSize(), theme = BackgroundTheme.ArrowStroke(
            stripes = listOf(
                Color(0xFFB3E5FC) to .7f,
                Color(0xFF81D4FA) to 1.5f,
                Color(0xFF4FC3F7) to 1.5f,
                Color(0xFF03A9F4) to 3f
            ),
            width = 800f,
            angle = 45f,
            animationDuration = 7800
        )
    )
}