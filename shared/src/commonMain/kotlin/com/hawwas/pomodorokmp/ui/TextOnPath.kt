package com.hawwas.pomodorokmp

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import kotlin.math.PI
import kotlin.math.atan2

@Composable
fun TextOnPath(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    progress: Float = 0f,
    createPath: (Size) -> Path
) {
    val textMeasurer = rememberTextMeasurer()
    val result = remember(text, style) {
        textMeasurer.measure(text = text, style = style)
    }
    
    val measure = remember { PathMeasure() }

    Canvas(modifier = modifier) {
        val path = createPath(size)
        measure.setPath(path, false)
        val pathLength = measure.length

        text.forEachIndexed { index, char ->
            val rect = result.getBoundingBox(index)
            val distance = (rect.left + (pathLength * progress)) % pathLength
            val pathOffset = measure.getPosition(distance)
            val tangent = measure.getTangent(distance)
            if (pathOffset != Offset.Unspecified && tangent != Offset.Unspecified) {
                val rotation = (atan2(tangent.y, tangent.x) * (180 / PI)).toFloat()

                rotate(
                    degrees = rotation,
                    pivot = pathOffset,
                ) {
                    drawText(
                        textMeasurer = textMeasurer,
                        text = char.toString(),
                        style = style,
                        topLeft = pathOffset - Offset(0f, rect.height * .5f),
                        size = Size(rect.width, rect.height)
                    )
                }
            }
        }
    }
}
