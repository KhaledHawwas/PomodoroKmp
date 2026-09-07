package com.hawwas.pomodorokmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun HexagonBackground(
    modifier: Modifier = Modifier,
    hexRadius: Dp = 32.dp,
    borderColor: Color = Color(0xFFC27E07),
    backgroundColor :Color = Color(0xFFF7AD3E),
    strokeWidth: Dp = 4.dp
) {
    val density = LocalDensity.current
    val radiusPx = with(density) { hexRadius.toPx() }
    val strokePx = with(density) { strokeWidth.toPx() }

    val hexPath = remember(radiusPx) { buildHexagonPath(radiusPx) }

    Box(
        modifier = modifier.background(backgroundColor).drawBehind {
            val hexHeight = sqrt(3f) * radiusPx
            val horizontalSpacing = radiusPx * 1.5f
            val verticalSpacing = hexHeight

            val cols = (size.width / horizontalSpacing).toInt() + 2
            val rows = (size.height / verticalSpacing).toInt() + 2

            for (col in -1..cols) {
                val x = col * horizontalSpacing
                val yOffset = if (col.mod(2) == 1) verticalSpacing / 2f else 0f
                for (row in -1..rows) {
                    val y = row * verticalSpacing + yOffset
                    translate(left = x, top = y) {
                        drawPath(hexPath, color = borderColor, style = Stroke(width = strokePx))
                    }
                }
            }
        }
    )
}
@Composable
@Preview
fun PreviewHexagonBackground() {
    HexagonBackground(
        modifier = Modifier.fillMaxSize(),
    )
}
private fun buildHexagonPath(radius: Float): Path = Path().apply {
    for (i in 0..5) {
        val angle = (60 * i).toDouble(). toRadians()
        val x = radius * cos(angle).toFloat()
        val y = radius * sin(angle).toFloat()
        if (i == 0) moveTo(x, y) else lineTo(x, y)
    }
    close()
}
