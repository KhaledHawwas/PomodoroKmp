package com.hawwas.pomodorokmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.min

@Composable
fun LEDSeconds() {

    var size by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    Box(
        Modifier
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF000000), Color(0xFF000000))
                    ),
                    blendMode = BlendMode.Softlight,
                )
            }
            .onSizeChanged {
                size = it
            }
            .fillMaxSize()
            .background(gray300),
        contentAlignment = Alignment.Center
    ) {

        Box(
            Modifier
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF000000), Color(0xFF000000))
                        ),
                        blendMode = BlendMode.Softlight,
                    )
                }
                .fillMaxSize()
                .background(gray300),
        )

        var time by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            while (true) {
                time = currentSeconds()
                delay(1000)
            }
        }

        val width by remember {
            derivedStateOf {
                with(density) { (min(size.width, size.height) / 3).toDp() }
            }
        }
        Row(
            modifier = Modifier
                .width(width * 2)
                .height(width * 2),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(width * .25f)
        ) {
            for (i in 0..1) {
                LEDDigit(
                    modifier = Modifier.weight(1f),
                    digit = time.getOrNull(i).let {
                        it?.toString()?.toInt() ?: 0
                    }
                )
            }
        }
    }
}

@Composable
fun LEDDigit(
    modifier: Modifier = Modifier,
    digit: Int,
) {

    val stems = intMap[digit]

    val density = LocalDensity.current
    var size by remember { mutableStateOf(IntSize.Zero) }
    val width by remember {
        derivedStateOf {
            with(density) { size.width.toDp() }
        }
    }

    Box(
        modifier = modifier
            .padding(4.dp)
            .onSizeChanged { size = it }
    ) {

        LEDSegment( // TOP RIGHT
            modifier = Modifier
                .offset(x = width / 2, y = -width / 2)
                .rotate(-90f).scale(-1f, 1f),
            on = stems?.getOrNull(2) == '1'
        )

        LEDSegment( // TOP
            modifier = Modifier.offset(y = -width),
            on = stems?.getOrNull(0) == '1'
        )

        LEDSegment( // BOTTOM RIGHT
            modifier = Modifier
                .offset(x = width / 2, y = width / 2)
                .rotate(-90f).scale(-1f, 1f),
            on = stems?.getOrNull(5) == '1'
        )

        LEDSegment( // MIDDLE
            on = stems?.getOrNull(3) == '1'
        )

        LEDSegment( // TOP LEFT
            modifier = Modifier
                .offset(x = -width / 2, y = -width / 2)
                .rotate(-90f).scale(-1f, 1f),
            on = stems?.getOrNull(1) == '1'
        )

        LEDSegment( // BOTTOM
            modifier = Modifier.offset(y = width),
            on = stems?.getOrNull(6) == '1'
        )

        LEDSegment( // BOTTOM LEFT
            modifier = Modifier
                .offset(x = -width / 2, y = width / 2)
                .rotate(-90f).scale(-1f, 1f),
            on = stems?.getOrNull(4) == '1'
        )
    }
}


@Composable
private fun LEDSegment(
    modifier: Modifier = Modifier,
    on: Boolean = false,
) {

    val animatedStemShadowOffset by animateDpAsState(
        targetValue = if (on) 5.dp else 0.dp,
        if (on)
            spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = Spring.DampingRatioMediumBouncy,
            )
        else
            spring(stiffness = Spring.StiffnessLow)
    )

    val animatedShadow by animateDpAsState(
        targetValue = if (on) 4.dp else 1.dp,
        spring(
            stiffness = Spring.StiffnessLow,
        )
    )

    val animatedSheenAlpha by animateFloatAsState(
        targetValue = if (on) 1f else .1f,
        spring(
            stiffness = Spring.StiffnessLow,
        )
    )

    val animatedSheenInfluence by animateFloatAsState(
        targetValue = if (on) 1f else 0f,
    )

    Box(
        modifier = modifier
            .fillMaxHeight(.12f)
            .fillMaxWidth(.9f)
    ) {
        Box(
            Modifier
                .offset(x = (-animatedStemShadowOffset / 2), y = (-animatedStemShadowOffset / 2))
                .fillMaxSize()
                .blur(animatedShadow, BlurredEdgeTreatment.Unbounded)
                .background(Color.White.copy(alpha = .65f), CutCornerShape(100))
        )

        Box(
            Modifier
                .offset(x = animatedStemShadowOffset, y = animatedStemShadowOffset)
                .fillMaxSize()
                .blur(animatedShadow, BlurredEdgeTreatment.Unbounded)
                .background(Color.Black.copy(alpha = .35f), CutCornerShape(100))
        )

        Box(
            Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = animatedSheenAlpha),
                            Color.Transparent,
                            Color.Black.copy(alpha = .04f),
                        )
                    ), shape = CutCornerShape(100)
                )
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            lerp(Color.Green, Color.Black, animatedSheenInfluence),
                            Color.Green,
                        )
                    ),
                    CutCornerShape(100)
                )
        )
    }
}


private fun currentSeconds(): String {
    val localTime = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return localTime.second.toString().padStart(2, '0')
}

private val intMap = mapOf(
    Pair(0, "1110111"),
    Pair(1, "0010010"),
    Pair(2, "1011101"),
    Pair(3, "1011011"),
    Pair(4, "0111010"),
    Pair(5, "1101011"),
    Pair(6, "1101111"),
    Pair(7, "1010010"),
    Pair(8, "1111111"),
    Pair(9, "1111011"),
)
@Composable
@Preview
fun prev(){
    LEDSeconds()
}

private const val baseFrequency = .4f
private const val size = 4


private val gray200 = Color(0xfffafafa)
private val gray300 = Color(0xffe5e5e5)