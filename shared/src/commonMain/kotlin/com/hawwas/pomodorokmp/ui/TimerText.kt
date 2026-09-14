package com.hawwas.pomodorokmp

import androidx.compose.runtime.Composable
import com.hawwas.pomodorokmp.model.TimerTextTheme
import kotlin.math.roundToInt

@Composable
fun TimerText(timeLeft: Int,timerTextTheme: TimerTextTheme, totalTime: Int) {
    val progress = timeLeft.toFloat() / totalTime
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val progressPercent = (progress * 100).roundToInt()
    when (timerTextTheme) {
        is TimerTextTheme.Solid -> {
            TimerTextSolid(minutes, seconds, progressPercent, timerTextTheme)
        }
    }

}
