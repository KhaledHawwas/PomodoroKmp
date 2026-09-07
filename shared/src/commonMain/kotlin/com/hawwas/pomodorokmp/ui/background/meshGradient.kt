package com.hawwas.pomodorokmp

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.VertexMode
import androidx.compose.ui.graphics.Vertices
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.Painter

data class MeshVertex(
    val position: Offset = Offset.Unspecified,
    val color: Color = Color.Transparent,
    val leftControlPoint: Offset = Offset.Unspecified,
    val topControlPoint: Offset = Offset.Unspecified,
    val rightControlPoint: Offset = Offset.Unspecified,
    val bottomControlPoint: Offset = Offset.Unspecified,
)

class MeshGradientScope internal constructor(
    private val vertices: Array<Array<MeshVertex>>,
) {
    fun setVertex(
        row: Int,
        column: Int,
        position: Offset,
        color: Color,
        leftControlPoint: Offset = Offset.Unspecified,
        topControlPoint: Offset = Offset.Unspecified,
        rightControlPoint: Offset = Offset.Unspecified,
        bottomControlPoint: Offset = Offset.Unspecified,
    ) {
        vertices[row][column] = MeshVertex(
            position = position,
            color = color,
            leftControlPoint = leftControlPoint,
            topControlPoint = topControlPoint,
            rightControlPoint = rightControlPoint,
            bottomControlPoint = bottomControlPoint,
        )
    }
}

class MeshGradientPainter(
    private val rows: Int,
    private val columns: Int,
    private val showPoints: Boolean = false,
    private val subdivisionsPerPatch: Int = 12,
    private val block: MeshGradientScope.() -> Unit,
) : Painter() {
    override val intrinsicSize: Size = Size.Unspecified

    private val vertices = Array(rows + 1) {
        Array(columns + 1) { MeshVertex() }
    }

    init {
        require(rows > 0) { "rows must be greater than 0" }
        require(columns > 0) { "columns must be greater than 0" }
        require(subdivisionsPerPatch > 0) { "subdivisionsPerPatch must be greater than 0" }
    }

    override fun DrawScope.onDraw() {
        MeshGradientScope(vertices).block()

        val positions = ArrayList<Offset>()
        val colors = ArrayList<Color>()
        val indices = ArrayList<Int>()
        val rowStride = subdivisionsPerPatch + 1

        for (row in 0 until rows) {
            for (column in 0 until columns) {
                val topLeft = vertices[row][column]
                val topRight = vertices[row][column + 1]
                val bottomLeft = vertices[row + 1][column]
                val bottomRight = vertices[row + 1][column + 1]

                val cellBase = positions.size

                for (y in 0..subdivisionsPerPatch) {
                    val v = y.toFloat() / subdivisionsPerPatch.toFloat()
                    val leftPosition = lerpOffset(topLeft.position, bottomLeft.position, v)
                    val rightPosition = lerpOffset(topRight.position, bottomRight.position, v)
                    val leftColor = lerpColor(topLeft.color, bottomLeft.color, v)
                    val rightColor = lerpColor(topRight.color, bottomRight.color, v)

                    for (x in 0..subdivisionsPerPatch) {
                        val u = x.toFloat() / subdivisionsPerPatch.toFloat()
                        val xPosition = lerp(leftPosition.x, rightPosition.x, u) * size.width
                        val yPosition = lerp(leftPosition.y, rightPosition.y, u) * size.height
                        positions.add(Offset(xPosition, yPosition))
                        colors.add(lerpColor(leftColor, rightColor, u))
                    }
                }

                for (y in 0 until subdivisionsPerPatch) {
                    for (x in 0 until subdivisionsPerPatch) {
                        val a = cellBase + (y * rowStride) + x
                        val b = a + 1
                        val c = a + rowStride
                        val d = c + 1
                        indices.add(a)
                        indices.add(c)
                        indices.add(d)
                        indices.add(a)
                        indices.add(b)
                        indices.add(d)
                    }
                }
            }
        }

        drawIntoCanvas { canvas ->
            canvas.drawVertices(
                vertices = Vertices(
                    vertexMode = VertexMode.Triangles,
                    positions = positions,
                    textureCoordinates = positions, // Must match positions.size
                    colors = colors,
                    indices = indices,
                ),
                // FIX: Use Dst (or Modulate) so Skia renders vertex colors instead of the white Paint
                blendMode = BlendMode.Dst,
                paint = Paint().apply {
                    color = Color.White
                    isAntiAlias = true
                },
            )

            if (showPoints) {
                val pointPaint = Paint().apply {
                    color = Color.White.copy(alpha = 0.9f)
                    isAntiAlias = true
                }
                // Scaled point positions to match screen dimensions
                val scaledPoints = vertices.flatten().map {
                    Offset(it.position.x * size.width, it.position.y * size.height)
                }
                canvas.drawPoints(
                    pointMode = PointMode.Points,
                    points = scaledPoints,
                    paint = pointPaint,
                )
            }
        }}}

private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + (stop - start) * fraction
}

private fun lerpOffset(start: Offset, stop: Offset, fraction: Float): Offset {
    return Offset(
        x = lerp(start.x, stop.x, fraction),
        y = lerp(start.y, stop.y, fraction),
    )
}

private fun lerpColor(start: Color, stop: Color, fraction: Float): Color {
    return Color(
        red = lerp(start.red, stop.red, fraction),
        green = lerp(start.green, stop.green, fraction),
        blue = lerp(start.blue, stop.blue, fraction),
        alpha = lerp(start.alpha, stop.alpha, fraction),
    )
}
