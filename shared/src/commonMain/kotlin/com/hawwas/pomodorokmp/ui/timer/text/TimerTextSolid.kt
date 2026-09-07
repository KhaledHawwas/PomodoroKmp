package com.hawwas.pomodorokmp

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.hawwas.pomodorokmp.model.TimerTextTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimerTextSolid(minutes: Int, seconds: Int, progressPercent: Int, theme: TimerTextTheme.Solid) {
    val timeSize = if (theme.showPercentage) 48.sp else 64.sp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            AnimatedContent(
                targetState = minutes.toString().padStart(2, '0'),
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { -it } togetherWith slideOutVertically { it }
                    } else {
                        slideInVertically { it } togetherWith slideOutVertically { -it }
                    }
                }
            ) { count ->
                Text(
                    count,
                    fontSize = timeSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = theme.color
                )
            }
            Text(
                ":",
                fontSize = timeSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = theme.color
            )
            AnimatedContent(
                targetState = seconds.toString().padStart(2, '0'),
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { -it } togetherWith slideOutVertically { it }
                    } else {
                        slideInVertically { it } togetherWith slideOutVertically { -it }
                    }
                }
            ) { count ->
                Text(
                    count,
                    fontSize = timeSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = theme.color
                )
            }

        }

        if (theme.showPercentage)
            Text(
                text = "$progressPercent%",
                fontSize = 24.sp,

                fontWeight = FontWeight.Bold,
                color = theme.color
            )
    }
}

