package com.hawwas.pomodorokmp.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hawwas.pomodorokmp.TimerRepository

@Composable
fun SettingsDrawer(
    isOpen: Boolean,
    timerRepository: TimerRepository,
    onSettingsChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isOpen,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it }),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(420.dp)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DURATIONS",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            var focusDuration by remember { mutableStateOf(timerRepository.focusDuration / 60) }
            var shortBreakDuration by remember { mutableStateOf(timerRepository.shortBreakDuration / 60) }
            var longBreakDuration by remember { mutableStateOf(timerRepository.longBreakDuration / 60) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DurationCard(
                    label = "POMODORO",
                    value = focusDuration,
                    onValueChange = { 
                        focusDuration = it
                        timerRepository.focusDuration = it * 60
                        onSettingsChanged()
                    },
                    modifier = Modifier.weight(1f)
                )

                DurationCard(
                    label = "BREAK",
                    value = shortBreakDuration,
                    onValueChange = { 
                        shortBreakDuration = it
                        timerRepository.shortBreakDuration = it * 60
                        onSettingsChanged()
                    },
                    modifier = Modifier.weight(1f)
                )

                DurationCard(
                    label = "LONG BREAK",
                    value = longBreakDuration,
                    onValueChange = { 
                        longBreakDuration = it
                        timerRepository.longBreakDuration = it * 60
                        onSettingsChanged()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun DurationCard(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember(value) { mutableStateOf(value.toString()) }

    Box(
        modifier = modifier
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BasicTextField(
                value = textValue,
                onValueChange = {
                    textValue = it
                    val intValue = it.toIntOrNull()
                    if (intValue != null && intValue > 0) {
                        onValueChange(intValue)
                    }
                },
                textStyle = MaterialTheme.typography.displaySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
