package com.hawwas.pomodorokmp.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hawwas.pomodorokmp.MeshGradientPainter
import com.hawwas.pomodorokmp.Wave

sealed class BackgroundTheme {
    data class Mesh(
        val animationDuration: Int?= null ,
        val meshPainter: (Float) -> MeshGradientPainter = { animationOffset ->
            MeshGradientPainter(rows = 3, columns = 3) {
                // Row 0
                setVertex(0, 0, Offset(0.0f, 0.0f), Color(0xFFFFE45C))
                setVertex(0, 1, Offset(0.33f, 0.0f), Color(0xFFFFD45F))
                setVertex(0, 2, Offset(0.66f, 0.0f), Color(0xFFFFB76E))
                setVertex(0, 3, Offset(1.0f, 0.0f), Color(0xFFF79A9E))

                // Row 1
                setVertex(1, 0, Offset(0.0f, 0.30f), Color(0xFFB8B7BD))
                setVertex(1, 1, Offset(0.33f, 0.30f), Color(0xFFE58FAF))
                setVertex(1, 2, Offset(0.66f, 0.30f), Color(0xFFE85BAF))
                setVertex(1, 3, Offset(1.0f, 0.30f), Color(0xFFE64EA9))

                // Row 2
                setVertex(2, 0, Offset(0.0f, 0.68f), Color(0xFF9EAFC2))
                setVertex(2, 1, Offset(0.33f, 0.68f), Color(0xFFE55CA9))
                setVertex(2, 2, Offset(0.66f, 0.68f), Color(0xFFE948A8))
                setVertex(2, 3, Offset(1.0f, 0.68f), Color(0xFFE744A7))

                // Row 3
                setVertex(3, 0, Offset(0.0f, 1.0f), Color(0xFF929FAE))
                setVertex(3, 1, Offset(0.33f, 1.0f), Color(0xFFD94DA5))
                setVertex(3, 2, Offset(0.66f, 1.0f), Color(0xFFE844AA))
                setVertex(3, 3, Offset(1.0f, 1.0f), Color(0xFFE747AA))
            }
        }
    ) : BackgroundTheme()

    data class Solid constructor(val color: Color) : BackgroundTheme()
    data class Waves constructor(
        val waves: List<Wave> = listOf(
            Wave(
                Color(0xFFB3E5FC),
                frequency = 2,
                shiftDuration = 2000,
                amplitude = 20..40,
                offset = 0.8f
            ),
            Wave(
                Color(0xFF81D4FA),
                frequency = 3,
                shiftDuration = 2600,
                amplitude = 30..50,
                offset = 0.7f
            ),
            Wave(
                Color(0xFF4FC3F7),
                frequency = 2,
                shiftDuration = 3200,
                amplitude = 25..45,
                offset = 0.6f
            ),
            Wave(
                Color(0xFF03A9F4),
                frequency = 3,
                shiftDuration = 3800,
                amplitude = 35..55,
                offset = 0.5f
            )
        ), val background: Color = Color.Black
    ) : BackgroundTheme()

    data class SolidStroke(
        val stripes: List<Pair<Color, Float>>,
        val width: Float = 20f,
        val angle: Float = 45f,
        val phase: Float = 0f,
    ) : BackgroundTheme()

    data class StarsSky(val color: Color) : BackgroundTheme()
    data class RepeatedRectangle constructor(
        val  baseColor: Color = Color(0xFF062408),
        val  glowColor: Color = Color.White,
        val  backgroundColor: Color= Color.Black,
        val  cellWidth: Dp = 66.dp,          // horizontal pitch of the grid
        val  cellHeight: Dp = 34.dp,         // vertical pitch of the grid
        val  rectWidth: Dp = 60.dp,          // drawn rectangle width  (< cellWidth leaves a gap)
        val  rectHeight: Dp = 18.dp,         // drawn rectangle height (< cellHeight leaves a gap)
        val  cornerRadius: Dp = 8.dp,
        val  glowRadius: Dp = 190.dp,        // distance at which a tile starts brightening
        val  dotCount: Int = 3,
        val  directionDegrees: Float = -25f, // single constant travel direction for every dot
    ): BackgroundTheme()

    data class ArrowStroke(
        val stripes: List<Pair<Color, Float>>,
        val width: Float = 20f,
        val angle: Float = 45f,
        val phase: Float = 0f,
        val animationDuration: Int?,
    ) : BackgroundTheme()
    data class Hexagon( val   hexRadius: Dp = 32.dp,
                        val   borderColor: Color = Color(0xFFC27E07),
                        val   backgroundColor :Color = Color(0xFFF7AD3E),
                        val   strokeWidth: Dp = 4.dp): BackgroundTheme()

}
