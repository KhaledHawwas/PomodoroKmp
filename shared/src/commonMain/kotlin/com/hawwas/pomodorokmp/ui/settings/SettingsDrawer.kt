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

private object SettingsLimits {
    const val MIN_DURATION_MINUTES = 1
    const val MAX_FOCUS_MINUTES = 60
    const val MAX_SHORT_BREAK_MINUTES = 30
    const val MAX_LONG_BREAK_MINUTES = 60
    const val MIN_INTERVAL = 1
    const val MAX_INTERVAL = 12
}

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

            var focusDuration by remember {
                mutableStateOf(
                    (timerRepository.focusDuration / 60)
                        .coerceIn(SettingsLimits.MIN_DURATION_MINUTES, SettingsLimits.MAX_FOCUS_MINUTES)
                )
            }
            var shortBreakDuration by remember {
                mutableStateOf(
                    (timerRepository.shortBreakDuration / 60)
                        .coerceIn(SettingsLimits.MIN_DURATION_MINUTES, SettingsLimits.MAX_SHORT_BREAK_MINUTES)
                )
            }
            var longBreakDuration by remember {
                mutableStateOf(
                    (timerRepository.longBreakDuration / 60)
                        .coerceIn(SettingsLimits.MIN_DURATION_MINUTES, SettingsLimits.MAX_LONG_BREAK_MINUTES)
                )
            }
            var longBreakInterval by remember {
                mutableStateOf(
                    timerRepository.longBreakInterval
                        .coerceIn(SettingsLimits.MIN_INTERVAL, SettingsLimits.MAX_INTERVAL)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DurationCard(
                    label = "POMODORO",
                    value = focusDuration,
                    minValue = SettingsLimits.MIN_DURATION_MINUTES,
                    maxValue = SettingsLimits.MAX_FOCUS_MINUTES,
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
                    minValue = SettingsLimits.MIN_DURATION_MINUTES,
                    maxValue = SettingsLimits.MAX_SHORT_BREAK_MINUTES,
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
                    minValue = SettingsLimits.MIN_DURATION_MINUTES,
                    maxValue = SettingsLimits.MAX_LONG_BREAK_MINUTES,
                    onValueChange = {
                        longBreakDuration = it
                        timerRepository.longBreakDuration = it * 60
                        onSettingsChanged()
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "CYCLES",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            DurationCard(
                label = "INTERVAL",
                value = longBreakInterval,
                minValue = SettingsLimits.MIN_INTERVAL,
                maxValue = SettingsLimits.MAX_INTERVAL,
                onValueChange = {
                    longBreakInterval = it
                    timerRepository.longBreakInterval = it
                    onSettingsChanged()
                },
                modifier = Modifier.width(120.dp)
            )
        }
    }
}

@Composable
fun DurationCard(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minValue: Int = 1,
    maxValue: Int = 99,
) {
    var textValue by remember(value) { mutableStateOf(value.toString()) }
    val maxDigits = maxValue.toString().length
    val isInvalid = textValue.isEmpty() ||
        textValue.toIntOrNull()?.let { it !in minValue..maxValue } == true

    Box(
        modifier = modifier
            .aspectRatio(0.95f)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BasicTextField(
                value = textValue,
                onValueChange = { newText ->
                    if (newText.isEmpty()) {
                        textValue = newText
                        return@BasicTextField
                    }
                    if (newText.any { !it.isDigit() }) return@BasicTextField
                    if (newText.length > maxDigits) return@BasicTextField
                    // Reject leading zeros (e.g. "01") while still allowing a temporary "0".
                    if (newText.length > 1 && newText.startsWith('0')) return@BasicTextField

                    textValue = newText
                    val intValue = newText.toIntOrNull() ?: return@BasicTextField
                    if (intValue in minValue..maxValue) {
                        onValueChange(intValue)
                    }
                },
                textStyle = MaterialTheme.typography.displaySmall.copy(
                    color = if (isInvalid) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
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
