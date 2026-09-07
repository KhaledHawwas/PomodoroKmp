package com.hawwas.pomodorokmp

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

@Composable
fun Light(
    mainGlowColor: Color = Color(0x3A2877FF),
    glowColor: Color = Color(0x3A0048FF),
    flip: Boolean = true,
    startY: Float,
    progress: Float= 20f,
    modifier: Modifier,
    halfTubeWidth: Float,
) {
    val animationProgress = lerp(0.5f, 1f, progress)

    Box(
        modifier = modifier
            .alpha(progress)
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithCache {
                val centerOffset = Offset(
                    x = size.width - halfTubeWidth,
                    y = startY
                )

                val sweepGradient1 = Brush.sweepGradient(
                    colorStops = arrayOf(
                        0.0f to mainGlowColor,
                        animationProgress * 0.49f to Color.Transparent,
                        1.0f to Color.Transparent,
                    ),
                    center = centerOffset
                )

                val sweepGradient2 = Brush.sweepGradient(
                    colorStops = arrayOf(
                        0.0f to glowColor,
                        0.0f to glowColor,
                        animationProgress * 0.35f to Color.Transparent,
                        1.0f to Color.Transparent,
                    ),
                    center = centerOffset
                )

                val start = 20.dp.toPx()
                val end = 450.dp.toPx()

                val mask = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to Color.White,
                        start / size.height to Color.White,
                        (start + (end - start) * 0.25f) / size.height to Color.White.copy(alpha = 0.7f),
                        (start + (end - start) * 0.55f) / size.height to Color.White.copy(alpha = 0.35f),
                        (start + (end - start) * 0.7f) / size.height to Color.White.copy(alpha = 0.15f),
                        end / size.height to Color.Transparent,
                        1f to Color.Transparent
                    )
                )

                onDrawBehind {
                    scale(
                        scaleX = if (flip) -1f else 1f,
                        scaleY = 1f
                    ) {
                        drawRect(
                            brush = sweepGradient1,
                            blendMode = BlendMode.Plus
                        )
                        drawRect(
                            brush = sweepGradient2,
                            blendMode = BlendMode.Plus
                        )
                    }

                    drawRect(
                        brush = mask,
                        blendMode = BlendMode.DstIn,
                        alpha = 0.98f
                    )
                }
            }
    )
}