package com.hawwas.pomodorokmp

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.time.Clock

@Composable
fun Shaker(
    text: String? = null,
    icon: Painter? = null,
    borderColor: Color = Color.White,
    color: Color,
    onClick: () -> Unit
) {
    val shake = remember { Animatable(0f) }
    var trigger by remember { mutableStateOf(0L) }
    LaunchedEffect(trigger) {
        if (trigger != 0L) {
            for (i in 0..10) {
                when (i % 2) {
                    0 -> shake.animateTo(7f, spring(stiffness = 100_000f))
                    else -> shake.animateTo(-7f, spring(stiffness = 100_000f))
                }
            }
            shake.animateTo(0f)
        }
    }

    Box(modifier = Modifier.clickable { trigger = Clock.System.now().epochSeconds; onClick() }
        .offset { IntOffset(x = shake.value.roundToInt(), y = 0) }
        .border(1.dp, borderColor, shape = MaterialTheme.shapes.medium)
        .padding(horizontal = 24.dp, vertical = 8.dp)) {
        text?.let {
            Text(text = it,color=color)
        }
        icon?.let {
            Icon(
                painter = icon,
                contentDescription = "Reset",
                tint = color,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}