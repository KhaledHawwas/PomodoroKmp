package com.hawwas.pomodorokmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hawwas.pomodorokmp.model.TimerShape
import com.hawwas.pomodorokmp.model.TimerTextTheme

@Composable
fun TimerRing(
    timeLeft: Int,
    totalTime: Int,
    timerShape: TimerShape,
    timerTextTheme: TimerTextTheme,
    modifier: Modifier = Modifier.size(400.dp, 120.dp)
) {
    val progress = (timeLeft.toFloat() / totalTime).coerceIn(0f, 1f)

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        when (timerShape) {
            is TimerShape.RoundedRect -> {
                ProgressRoundedRectOutline(
                    progress = progress,
                    strokeWidth = 8.dp,
                    cornerRadius = 24.dp,
                    trackColor = timerShape.emptyBarColor,
                    progressColor = timerShape.mainColor,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is TimerShape.Circle -> {
                ProgressCircle(progress, timerShape)
            }

            TimerShape.None -> {}
        }

        TimerText(
            timeLeft,
            timerTextTheme ,
            totalTime,
        )
    }
}