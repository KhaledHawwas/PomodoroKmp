package com.hawwas.pomodorokmp

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Repeating 45°-rotated rounded-rect grid. 2–3 invisible dots roam the canvas;
 * tiles lerp from [baseColor] toward [glowColor] the closer they are to a dot.
 *
 * Perf notes:
 * - The whole canvas is rotated ONCE (`rotate(45f)`), not each rect — avoids
 *   per-cell withTransform allocations/matrix pushes.
 * - Dot positions are recomputed once per frame (tiny array, size = dotCount),
 *   not per cell.
 * - Everything else is primitive math in a flat double loop, no per-cell
 *   object allocation beyond the two Offsets used for drawing.
 */
@Composable
fun RoundedRectGridBackground(
    modifier: Modifier = Modifier,
    baseColor: Color = Color(0xFF1B5E20),
    glowColor: Color = Color.White,
    backgroundColor: Color= Color.Black,
    cellWidth: Dp = 66.dp,          // horizontal pitch of the grid
    cellHeight: Dp = 34.dp,         // vertical pitch of the grid
    rectWidth: Dp = 60.dp,          // drawn rectangle width  (< cellWidth leaves a gap)
    rectHeight: Dp = 18.dp,         // drawn rectangle height (< cellHeight leaves a gap)
    cornerRadius: Dp = 8.dp,
    glowRadius: Dp = 150.dp,        // distance at which a tile starts brightening
    dotCount: Int = 3,
    directionDegrees: Float = -25f, // single constant travel direction for every dot
) {
    val transition = rememberInfiniteTransition(label = "proximityDots")

    // Different durations only vary speed/phase; direction never changes.
    val dotDurations = remember(dotCount) { List(dotCount) { i -> 9000 + i * 3100 } }
    val dotTs = dotDurations.mapIndexed { i, duration ->
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(duration, easing = LinearEasing)),
            label = "dotT$i"
        )
    }

    // Lanes offset from center (perpendicular to travel direction), non-zero
    // and alternating sides, so no dot's path ever crosses the exact middle.
    val laneFractions = remember(dotCount) {
        List(dotCount) { i ->
            val sign = if (i % 2 == 0) 1f else -1f
            sign * (0.8f + i * 0.26f)
        }
    }


    val theta = remember(directionDegrees) { directionDegrees.toDouble().toRadians().toFloat() }
    val dirX = cos(theta)
    val dirY = sin(theta)
    val perpX = -dirY
    val perpY = dirX

    Canvas(modifier = modifier.fillMaxSize().background(backgroundColor)) {
        val cellWPx = cellWidth.toPx()
        val cellHPx = cellHeight.toPx()
        val rectWPx = rectWidth.toPx()
        val rectHPx = rectHeight.toPx()
        val cornerPx = cornerRadius.toPx()
        val glowPx = glowRadius.toPx()

        // After rotating the canvas by 45°, screen corners land outside the
        // original bounds -> cover a square sized to the diagonal instead.
        val diagonal = hypot(size.width, size.height)
        val half = diagonal / 2f
        // Travel amplitude comfortably beyond the square's corner-to-corner
        // extent so the wrap-around jump happens off the drawn grid.
        val amplitude = half * 1.5f

        rotate(degrees = 45f) {
            val dots = Array(dotTs.size) { i ->
                val t = dotTs[i].value
                val travel = amplitude * (2f * t - 1f)   // -amplitude .. +amplitude, one direction
                val lane = half * laneFractions[i]
                Offset(
                    x = center.x + perpX * lane + dirX * travel,
                    y = center.y + perpY * lane + dirY * travel,
                )
            }

            val cols = (diagonal / cellWPx).toInt() + 2
            val rows = (diagonal / cellHPx).toInt() + 2
            val startX = center.x - half
            val startY = center.y - half

            for (row in 0..rows) {
                val cy = startY + row * cellHPx + cellHPx / 2f
                for (col in 0..cols) {
                    val cx = startX + col * cellWPx + cellWPx / 2f

                    var minDist = Float.MAX_VALUE
                    for (dot in dots) {
                        val dx = cx - dot.x
                        val dy = cy - dot.y
                        val d = sqrt(dx * dx + dy * dy)
                        if (d < minDist) minDist = d
                    }

                    val glow = 1f - (minDist / glowPx).coerceIn(0f, 1f)
                    val color = lerp(baseColor, glowColor, glow)

                    drawRoundRect(
                        color = color,
                        topLeft = Offset(cx - rectWPx / 2f, cy - rectHPx / 2f),
                        size = Size(rectWPx, rectHPx),
                        cornerRadius = CornerRadius(cornerPx, cornerPx),
                    )
                }
            }
        }
    }
}
fun Double.toRadians(): Double = this * (PI / 180.0)
@Preview
@Composable
fun proxPreview(){
    RoundedRectGridBackground(modifier = Modifier.fillMaxSize())
}
