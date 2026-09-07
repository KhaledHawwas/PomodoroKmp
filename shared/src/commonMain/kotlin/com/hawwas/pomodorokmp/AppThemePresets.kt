package com.hawwas.pomodorokmp

import androidx.compose.ui.graphics.Color
import com.hawwas.pomodorokmp.model.BackgroundTheme
import com.hawwas.pomodorokmp.model.SurroundedTextTheme
import com.hawwas.pomodorokmp.model.TimerShape
import com.hawwas.pomodorokmp.model.TimerTextTheme
import com.hawwas.pomodorokmp.model.ActionButtonTheme

object AppThemePresets {

    private enum class ActionButtonStyle { Solid, Outlined, IconOnly }

    private fun createAppTheme(
        backgroundTheme: BackgroundTheme,
        isDark: Boolean,
        name: String? = null
    ): AppTheme {
        val textColor = if (isDark) Color.White else Color.DarkGray
        val emptyBarColor = if (isDark) Color.White.copy(alpha = 0.2f) else Color.DarkGray.copy(alpha = 0.2f)
        val onAccent = if (isDark) Color.Black else Color.White
        val stopColor = Color(0xFFB3261E) // Error red
        val actionButtonStyle: ActionButtonStyle = ActionButtonStyle.Solid

        val actionButtonTheme: ActionButtonTheme = when (actionButtonStyle) {
            ActionButtonStyle.Solid -> ActionButtonTheme.Solid(
                startContainerColor = textColor,
                startContentColor = onAccent,
                stopContainerColor = stopColor,
                stopContentColor = Color.White,
                resetContentColor = textColor
                , contentColor = textColor
            )
            ActionButtonStyle.Outlined -> ActionButtonTheme.Outlined(
                startBorderColor = textColor,
                startContentColor = textColor,
                stopBorderColor = stopColor,
                stopContentColor = stopColor,
                resetContentColor = textColor
                , contentColor = textColor
            )
            ActionButtonStyle.IconOnly -> ActionButtonTheme.IconOnly(
                startContainerColor = textColor,
                startContentColor = onAccent,
                stopContainerColor = stopColor,
                stopContentColor = Color.White,
                resetContentColor = textColor
                , contentColor = textColor
            )
        }

        return AppTheme(
            backgroundTheme = backgroundTheme,
            surroundedTextTheme = SurroundedTextTheme.None,
            timerTextTheme = TimerTextTheme.Solid(color = textColor, showPercentage = true),
            timeShape = TimerShape.Circle(
                mainColor = textColor,
                emptyBarColor = emptyBarColor
            ),
            actionButtonTheme = actionButtonTheme,
            name = name
        )
    }

    // ---------- Solid ----------
    val solidCrimson = createAppTheme(BackgroundThemePresets.solidCrimson, isDark = true, name = "Crimson Tide")

    val solids = listOf(solidCrimson, )

    // ---------- SolidStroke ----------
    val stripesCandy = createAppTheme(BackgroundThemePresets.stripesCandy, isDark = true , name = "Candy Stripes")

    val solidStrokes = listOf(stripesCandy, )

    // ---------- ArrowSolidStroke ----------
    val chevronElectric = createAppTheme(BackgroundThemePresets.chevronHazard, isDark = true, name = "Electric Strike")

    val arrowSolidStrokes = listOf( chevronElectric)

    // ---------- StarsSky ----------
    val starsDeepSpace = createAppTheme(BackgroundThemePresets.starsDeepSpace, isDark = true, name = "Deep Space")
    val starsNebulaPurple = createAppTheme(BackgroundThemePresets.starsNebulaPurple, isDark = true, name = "Nebula Purple")
    val starsMidnightBlue = createAppTheme(BackgroundThemePresets.starsMidnightBlue, isDark = true, name = "Midnight Blue")
    val starsVoidBlack = createAppTheme(BackgroundThemePresets.starsVoidBlack, isDark = true, name = "Void Black")
    val starsAuroraGreen = createAppTheme(BackgroundThemePresets.starsNebulaPurple, isDark = true, name = "Aurora Green")

    val starSkies = listOf(
        starsDeepSpace, starsNebulaPurple, starsMidnightBlue, starsVoidBlack, starsAuroraGreen
    )

    // ---------- Waves ----------
    val wavesOcean = createAppTheme(BackgroundThemePresets.wavesOcean, isDark = false, name = "Ocean Waves")
    val wavesSunset = createAppTheme(BackgroundThemePresets.wavesSunset, isDark = true, name = "Sunset Glow")
    val wavesForest = createAppTheme(BackgroundThemePresets.wavesForest, isDark = true, name = "Forest Whispers")
    val wavesFire = createAppTheme(BackgroundThemePresets.wavesFire, isDark = true, name = "Burning Fire")

    val waves = listOf(wavesOcean, wavesSunset, wavesForest, wavesFire)

    // ---------- Mesh ----------
    val meshAurora = createAppTheme(BackgroundThemePresets.meshAurora, isDark = true, name = "Aurora Borealis")
    val meshCandy = createAppTheme(BackgroundThemePresets.mesh2, isDark = false, name = "Candy Mesh")
    val mesh3 = createAppTheme(BackgroundThemePresets.mesh3, isDark = false, name = "Candy Mesh")

    val meshes = listOf(meshAurora, meshCandy, mesh3)

    //
    val rect =createAppTheme(backgroundTheme = BackgroundTheme.RepeatedRectangle() ,isDark = true, name = "Infinite Rectangles")
    val hex =createAppTheme(backgroundTheme = BackgroundTheme.Hexagon() ,isDark = true , name = "Infinite Hexagon")


    // ---------- Everything, for a picker/gallery screen ----------
    val all: List<AppTheme> =
        solids + solidStrokes + arrowSolidStrokes + starSkies + waves + meshes +rect+hex
}