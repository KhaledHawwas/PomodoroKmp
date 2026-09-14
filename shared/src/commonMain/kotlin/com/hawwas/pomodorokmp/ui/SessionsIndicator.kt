package com.hawwas.pomodorokmp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hawwas.pomodorokmp.TimerMode

@Composable
fun SessionIndicator(
    completedSessions: Int,
    totalSessions: Int,
    mode: TimerMode,
    activeColor: Color,
    inactiveColor: Color,
    modifier: Modifier = Modifier
) {
    val currentCycleSession = if (mode == TimerMode.LONG_BREAK) {
        totalSessions
    } else {
        completedSessions % totalSessions
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalSessions) { index ->
            val isCompleted = index < currentCycleSession
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) activeColor else inactiveColor)
            )
        }
    }
}
