package com.hawwas.pomodorokmp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.floor
import kotlin.random.Random

private data class Star(
    var xRatio: Float,
    var yRatio: Float,
    val alpha: Float,
    val radius: Float,
    val speed: Float
)

    var canvasSize =IntSize.Zero
    val frameTime =  mutableLongStateOf(0L)
@Composable
fun MovingStarsBackground(
    modifier: Modifier = Modifier,
    starCount: Int = 50,
    baseSpeed: Float = 0.03f, // Screen percentage moved per second
    minStarRadius: Dp = 1.dp,
    maxStarRadius: Dp = 2.5.dp,
    background:Color

) {

    // Frame tick state to trigger canvas redrawing without triggering full layout recomposition

    // Pre-generate stars with normalized coordinates (0.0 to 1.0)
    val stars = remember(starCount) {
        List(starCount) {
            Star(
                xRatio = Random.nextFloat(),
                yRatio = Random.nextFloat(),
                alpha = Random.nextFloat().coerceIn(0.3f, 1.0f),
                radius = Random.nextDouble(
                    minStarRadius.value.toDouble(),
                    maxStarRadius.value.toDouble()
                ).toFloat(),
                speed = baseSpeed * Random.nextDouble(0.9, 1.3).toFloat()
            )
        }
    }

    // Animation Loop
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            var lastTime = 0L
            while (true) {
                withFrameNanos { nanos ->
                    if (lastTime != 0L) {
                        val dt =
                            (nanos - lastTime) / 1_000_000_000f // Convert nanoseconds to seconds
                        stars.forEach { star ->
                            // Move star upwards
                            star.yRatio -= star.speed * dt

                            // Loop around to bottom when moving off screen top
                            if (star.yRatio < 0f) {
                                star.yRatio = 1f -(-star.yRatio - floor(-star.yRatio))
                                star.xRatio = Random.nextFloat() // Randomize X position on wrap
                            }
                        }
                    }
                    lastTime = nanos
                    frameTime.longValue = nanos // Trigger canvas redraw
                }
            }
        }
    }
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(background)
            .onSizeChanged { canvasSize = it }
    ) {
        // Accessing frameTime value links canvas rendering to frame loop
        @Suppress("UNUSED_VARIABLE")
        val drawTick = frameTime.longValue

        if (canvasSize.width == 0 || canvasSize.height == 0) return@Canvas

        stars.forEach { star ->
            val pxX = star.xRatio * canvasSize.width
            val pxY = star.yRatio * canvasSize.height

            drawCircle(
                color = Color.White.copy(alpha = star.alpha),
                radius = star.radius.dp.toPx(),
                center = Offset(pxX, pxY)
            )
        }
    }
}