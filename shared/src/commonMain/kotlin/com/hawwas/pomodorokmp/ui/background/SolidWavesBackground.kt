package com.hawwas.pomodorokmp

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

import androidx.compose.animation.core.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

data class Wave(val color: Color,val frequency: Int, val shiftDuration:Int, val amplitude : IntRange, val offset:Float)

@Composable
fun SolidWavesBackground(
    modifier: Modifier = Modifier,
    background: Color,
    waves: List<Wave> = listOf(
        Wave(Color(0xFFB3E5FC), frequency = 2, shiftDuration = 2000, amplitude = 20..40, offset = 0.8f),
        Wave(Color(0xFF81D4FA), frequency = 3, shiftDuration = 2600, amplitude = 30..50, offset = 0.7f),
        Wave(Color(0xFF4FC3F7), frequency = 2, shiftDuration = 3200, amplitude = 25..45, offset = 0.6f),
        Wave(Color(0xFF03A9F4), frequency = 3, shiftDuration = 3800, amplitude = 35..55, offset = 0.5f)
    ),
) {
    val sortedWaves = remember(waves) { waves.sortedByDescending { it.offset } }
    val transition = rememberInfiniteTransition(label = "wave_transition")

    val phaseShifts = sortedWaves.mapIndexed { index, wave ->
        transition.animateFloat(
            initialValue = 0f,
            targetValue = (2 * PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = wave.shiftDuration, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "phaseShift_$index"
        )
    }

    val amplitudes = sortedWaves.mapIndexed { index, wave ->
        transition.animateFloat(
            initialValue = wave.amplitude.first.toFloat(),
            targetValue = wave.amplitude.last.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = wave.shiftDuration, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "amplitude_$index"
        )
    }

    var canvasSize by remember { mutableStateOf(Size.Zero) }

    // Reusable background paths allocation
    val paths = remember(sortedWaves.size) { List(sortedWaves.size) { Path() } }

    // Invalidation tick state to signal Compose DrawScope to redraw on new frame
    var drawTick by remember { mutableLongStateOf(0L) }

    LaunchedEffect(sortedWaves, canvasSize) {
        if (canvasSize.width <= 0f || canvasSize.height <= 0f) return@LaunchedEffect

        snapshotFlow {
            // Read active animation states every frame
            val currentPhases = phaseShifts.map { it.value }
            val currentAmplitudes = amplitudes.map { it.value }
            currentPhases to currentAmplitudes
        }
            .flowOn(Dispatchers.Default)
            .collect { (phases, amps) ->
                // Compute geometry off UI thread directly into pre-allocated paths
                sortedWaves.forEachIndexed { index, wave ->
                    val yPos = (1 - wave.offset) * canvasSize.height
                    val path = paths[index]
                    path.reset()

                    val phaseOffset = index * (PI / 2).toFloat()

                    prepareSinePath(
                        path = path,
                        size = canvasSize,
                        frequency = wave.frequency,
                        amplitude = amps[index],
                        phaseShift = phases[index] + phaseOffset,
                        position = yPos,
                        step = 20
                    )

                    path.lineTo(canvasSize.width, canvasSize.height)
                    path.lineTo(0f, canvasSize.height)
                    path.close()
                }

                // Increment state counter to force drawPhase invalidation cleanly
                drawTick++
            }
    }

    Box(
        modifier = modifier
            .background(background)
            .onSizeChanged { size ->
                canvasSize = Size(size.width.toFloat(), size.height.toFloat())
            }
            .drawBehind {
                // Reading drawTick inside DrawScope subscribes it to state changes without full recomposition
                @Suppress("UNUSED_VARIABLE")
                val tick = drawTick

                paths.forEachIndexed { index, path ->
                    if (index < sortedWaves.size) {
                        drawPath(path = path, color = sortedWaves[index].color, style = Fill)
                    }
                }
            }
    )
}
fun prepareSinePath(
    path: Path,
    size: Size,
    frequency: Int,
    amplitude: Float,
    phaseShift: Float,
    position: Float,
    step: Int
) {
    for (x in 0..size.width.toInt().plus(step) step step) {
        val y = position + amplitude * sin(x * frequency * PI / size.width + phaseShift).toFloat()
        if (path.isEmpty)
            path.moveTo(x.toFloat(), max(0f, min(y, size.height)))
        else
            path.lineTo(x.toFloat(), max(0f, min(y, size.height)))
    }
}

@Preview
@Composable
fun SolidWavesPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Background behind the waves
    ) {
        // Set progress = 1f if you want the waves to fill the entire screen top-to-bottom
        SolidWavesBackground(
            background = Color.Black,

            modifier = Modifier.fillMaxSize(),
        )
    }
}