package com.hawwas.pomodorokmp

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProgressRoundedRectOutline(
    progress: Float,
    strokeWidth: Dp = 8.dp,
    cornerRadius: Dp = 24.dp,
    trackColor: Color = Color.LightGray.copy(alpha = 0.4f),
    progressColor: Color = Color(0xFF6200EE),
    modifier: Modifier = Modifier
) {
    val pathMeasure = remember { PathMeasure() }
    val path = remember { Path() }
    val progressPath = remember { Path() }

    Canvas(modifier = modifier) {
        val strokePx = strokeWidth.toPx()
        val cornerPx = cornerRadius.toPx()

        // Inset by half the stroke width so the border stays completely inside bounds
        val rectSize = Size(
            width = size.width - strokePx,
            height = size.height - strokePx
        )

        path.reset()
        path.addRoundRect(
            RoundRect(
                left = strokePx / 2,
                top = strokePx / 2,
                right = strokePx / 2 + rectSize.width,
                bottom = strokePx / 2 + rectSize.height,
                cornerRadius = CornerRadius(cornerPx, cornerPx)
            )
        )

        // 1. Draw Background Track
        drawPath(
            path = path,
            color = trackColor,
            style = Stroke(width = strokePx)
        )

        // 2. Measure full path length and calculate partial stroke
        pathMeasure.setPath(path, false)
        val totalLength = pathMeasure.length
        val currentLength = totalLength * progress

        progressPath.reset()
        pathMeasure.getSegment(
            startDistance = 0f,
            stopDistance = currentLength,
            destination = progressPath,
            startWithMoveTo = true
        )

        // 3. Draw Active Progress Line
        drawPath(
            path = progressPath,
            color = progressColor,
            style = Stroke(
                width = strokePx,
                cap = StrokeCap.Round
            )
        )
    }
}

