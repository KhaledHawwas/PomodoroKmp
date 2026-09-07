package com.hawwas.pomodorokmp

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.hawwas.pomodorokmp.model.BackgroundTheme

object BackgroundThemePresets {

    // ---------- Solid ----------
    val solidCrimson = BackgroundTheme.Solid(Color(0xFFFF8585))


    val solids = listOf(
        solidCrimson
    )

    // ---------- SolidStroke (static diagonal stripes) ----------
    val stripesCandy = BackgroundTheme.SolidStroke(
        listOf(
            Color(0xffc76b98) to 1f,
            Color(0xfff09f9c) to 1f,
            Color(0xFFfcc3a3) to 1f,
        ), width = 245f, angle = 45f
    )


    val solidStrokes = listOf(
        stripesCandy
    )

    // ---------- ArrowSolidStroke (scrolling chevrons, meeting in the middle) ----------
    val chevronHazard = BackgroundTheme.ArrowStroke(
        stripes = listOf(
            Color(0xFFB3E5FC) to .7f,
            Color(0xFF81D4FA) to 1.5f,
            Color(0xFF4FC3F7) to 1.5f,
            Color(0xFF03A9F4) to 3f
        ),
        width = 800f,
        angle = 45f,
        animationDuration = 7800
    )

    val arrowSolidStrokes = listOf(
        chevronHazard
    )

    // ---------- StarsSky ----------
    val starsDeepSpace = BackgroundTheme.StarsSky(Color(0xFF0D1B2A))
    val starsNebulaPurple = BackgroundTheme.StarsSky(Color(0xFF2E1A47))
    val starsMidnightBlue = BackgroundTheme.StarsSky(Color(0xFF0A0E27))
    val starsVoidBlack = BackgroundTheme.StarsSky(Color(0xFF000000))

    val starSkies = listOf(
        starsDeepSpace, starsNebulaPurple, starsMidnightBlue, starsVoidBlack
    )

    // ---------- Waves ----------
    val wavesOcean = BackgroundTheme.Waves() // your original — kept as default

    val wavesSunset = BackgroundTheme.Waves(
        waves = listOf(
            Wave(
                Color(0xFFFFCC80),
                frequency = 2,
                shiftDuration = 2200,
                amplitude = 20..40,
                offset = 0.9f
            ),
            Wave(
                Color(0xFFFF8A65),
                frequency = 3,
                shiftDuration = 2800,
                amplitude = 30..50,
                offset = 0.4f
            ),
            Wave(
                Color(0xFFFF5252),
                frequency = 2,
                shiftDuration = 3400,
                amplitude = 25..45,
                offset = 0.3f
            ),
            Wave(
                Color(0xFF6A1B9A),
                frequency = 3,
                shiftDuration = 4000,
                amplitude = 35..55,
                offset = 0.2f
            )
        ),
        background = Color(0xFF1A0033)
    )

    val wavesForest = BackgroundTheme.Waves(
        waves = listOf(
            Wave(
                Color(0xFFA5D6A7),
                frequency = 3,
                shiftDuration = 8000,
                amplitude = 20..40,
                offset = 0.94f
            ),
            Wave(
                Color(0xFF66BB6A),
                frequency = 3,
                shiftDuration = 2600,
                amplitude = 30..50,
                offset = 0.6f
            ),
            Wave(
                Color(0xFF388E3C),
                frequency = 2,
                shiftDuration = 3200,
                amplitude = 25..45,
                offset = 0.4f
            ),
            Wave(
                Color(0xFF1B5E20),
                frequency = 3,
                shiftDuration = 3800,
                amplitude = 35..55,
                offset = 0.3f
            )
        ),
        background = Color(0xFF0A1F0A)
    )

    val wavesFire = BackgroundTheme.Waves(
        waves = listOf(
            Wave(
                Color(0xFFFFD54F),
                frequency = 3,
                shiftDuration = 1800,
                amplitude = 20..25,
                offset = 0.94f
            ),
            Wave(
                Color(0xFFFF8F00),
                frequency = 4,
                shiftDuration = 2200,
                amplitude = 10..12,
                offset = 0.84f
            ),
            Wave(
                Color(0xFFE64A19),
                frequency = 3,
                shiftDuration = 2600,
                amplitude = 10..12,
                offset = 0.75f
            ),
            Wave(
                Color(0xFFB71C1C),
                frequency = 4,
                shiftDuration = 3000,
                amplitude = 35..40,
                offset = 0.68f
            )
        ),
        background = Color(0xFF1A0000)
    )

    val waves = listOf(wavesOcean, wavesSunset, wavesForest, wavesFire)

    // ---------- Mesh ----------
    // NOTE: names must match whatever your MeshGradient() lookup expects —
    // swap these strings for your actual preset keys.
    val meshAurora = BackgroundTheme.Mesh()
    val mesh2 = BackgroundTheme.Mesh(meshPainter = { animationOffset ->
        MeshGradientPainter(rows = 3, columns = 3) {
            // Row 0
            setVertex(0, 0, Offset(0.0f, 0.0f), Color(0xFF9D5FE5))
            setVertex(0, 1, Offset(0.33f, 0.0f), Color(0xFFB26AE9))
            setVertex(0, 2, Offset(0.66f, 0.0f), Color(0xFFB56CE7))
            setVertex(0, 3, Offset(1.0f, 0.0f), Color(0xFFA967DF))

            // Row 1
            setVertex(1, 0, Offset(0.0f, 0.30f), Color(0xFF9089E2))
            setVertex(1, 1, Offset(0.33f, 0.30f), Color(0xFF8DA6E8))
            setVertex(1, 2, Offset(0.66f, 0.30f), Color(0xFF91A9E8))
            setVertex(1, 3, Offset(1.0f, 0.30f), Color(0xFFA09CE4))

            // Row 2
            setVertex(2, 0, Offset(0.0f, 0.68f), Color(0xFF8CAFE5))
            setVertex(2, 1, Offset(0.33f, 0.68f), Color(0xFF9DBBE9))
            setVertex(2, 2, Offset(0.66f, 0.68f), Color(0xFFB1B9E5))
            setVertex(2, 3, Offset(1.0f, 0.68f), Color(0xFFC1AFE1))

            // Row 3
            setVertex(3, 0, Offset(0.0f, 1.0f), Color(0xFFA8B9E3))
            setVertex(3, 1, Offset(0.33f, 1.0f), Color(0xFFB9BCE3))
            setVertex(3, 2, Offset(0.66f, 1.0f), Color(0xFFCBB4DF))
            setVertex(3, 3, Offset(1.0f, 1.0f), Color(0xFFD3A9DE))
        }
    })
    val mesh3 = BackgroundTheme.Mesh(meshPainter = { animationOffset ->
        MeshGradientPainter(rows = 3, columns = 3) {

                // Row 0
                setVertex(0, 0, Offset(0.0f, 0.0f), Color(0xFFFF5BDA))
                setVertex(0, 1, Offset(0.33f, 0.0f), Color(0xFFFF5CD9))
                setVertex(0, 2, Offset(0.66f, 0.0f), Color(0xFFFF65DD))
                setVertex(0, 3, Offset(1.0f, 0.0f), Color(0xFFF87BD6))

                // Row 1
                setVertex(1, 0, Offset(0.0f, 0.30f), Color(0xFFFF80DC))
                setVertex(1, 1, Offset(0.33f, 0.30f), Color(0xFFF7A9D5))
                setVertex(1, 2, Offset(0.66f, 0.30f), Color(0xFFBFD6D5))
                setVertex(1, 3, Offset(1.0f, 0.30f), Color(0xFFA1DDD2))

                // Row 2
                setVertex(2, 0, Offset(0.0f, 0.68f), Color(0xFF8FE0D7))
                setVertex(2, 1, Offset(0.33f, 0.68f), Color(0xFF55DDD9))
                setVertex(2, 2, Offset(0.66f, 0.68f), Color(0xFF39E1DC))
                setVertex(2, 3, Offset(1.0f, 0.68f), Color(0xFF35E1DC))

                // Row 3
                setVertex(3, 0, Offset(0.0f, 1.0f), Color(0xFF58E1D9))
                setVertex(3, 1, Offset(0.33f, 1.0f), Color(0xFF35E3DF))
                setVertex(3, 2, Offset(0.66f, 1.0f), Color(0xFF2DE3DF))
                setVertex(3, 3, Offset(1.0f, 1.0f), Color(0xFF2DE1DE))
            }
    })


    val meshes = listOf(meshAurora)

    // ---------- Everything, for a picker/gallery screen ----------
    val all: List<BackgroundTheme> =
        solids + solidStrokes + arrowSolidStrokes + starSkies + waves + meshes
}