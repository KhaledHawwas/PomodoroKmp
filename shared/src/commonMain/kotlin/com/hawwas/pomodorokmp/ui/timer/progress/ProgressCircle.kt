package com.hawwas.pomodorokmp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.hawwas.pomodorokmp.model.TimerShape

@Composable
fun ProgressCircle(progress: Float, theme: TimerShape.Circle) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Background Circle
        drawCircle(
            color = theme.emptyBarColor,
            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
        )
        // Progress Arc
        drawArc(
            color = theme.mainColor,
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Butt)
        )
    }
}
