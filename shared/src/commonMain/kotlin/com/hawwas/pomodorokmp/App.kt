package com.hawwas.pomodorokmp

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import com.hawwas.pomodorokmp.ui.SessionIndicator
import com.hawwas.pomodorokmp.ui.settings.SettingsDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.hawwas.pomodorokmp.model.BackgroundTheme
import com.hawwas.pomodorokmp.model.SurroundedTextTheme
import com.hawwas.pomodorokmp.model.TimerTextTheme
import com.hawwas.pomodorokmp.model.TimerShape
import com.hawwas.pomodorokmp.model.ActionButtonTheme
import kotlin.time.Clock

import org.jetbrains.compose.resources.painterResource
import pomodorokmp.shared.generated.resources.Res
import pomodorokmp.shared.generated.resources.fullscreen_exit
import pomodorokmp.shared.generated.resources.fullscreen_fill
import pomodorokmp.shared.generated.resources.pause
import pomodorokmp.shared.generated.resources.photo_prints
import pomodorokmp.shared.generated.resources.play_arrow_fill
import pomodorokmp.shared.generated.resources.refresh
import pomodorokmp.shared.generated.resources.skip_next
import kotlin.math.sqrt


@Composable
fun App(
    isFullscreen: Boolean = false,
    onToggleFullscreen: (() -> Unit)? = null
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFF121212)
        )
    ) {

        Background({ Content(isFullscreen,onToggleFullscreen,it) })
    }
}


data class AppTheme constructor(
    val backgroundTheme: BackgroundTheme,
    val surroundedTextTheme: SurroundedTextTheme = SurroundedTextTheme.None,
    val timerTextTheme: TimerTextTheme = TimerTextTheme.Solid(
        Color.DarkGray,
        showPercentage = true
    ),
    val timeShape: TimerShape = TimerShape.Circle(
        mainColor = Color.DarkGray,
        emptyBarColor = Color.LightGray.copy(alpha = 0.4f)
    ),
    val actionButtonTheme: ActionButtonTheme = ActionButtonTheme.Solid(
        startContainerColor = Color(0xFF6200EE),
        startContentColor = Color.White,
        stopContainerColor = Color(0xFFB3261E),
        stopContentColor = Color.White,
        resetContentColor = Color.White,
        contentColor = Color.White
    ),
    val name: String? = null,
    val isDark: Boolean=false ,
)

private val repo = TimerRepository(clock = Clock.System)
var i by mutableStateOf(repo.themeIndex)

var appTheme by mutableStateOf(
    AppThemePresets.all[i.coerceIn(AppThemePresets.all.indices)]
)


private var lastAppTheme: AppTheme = appTheme
private var appThemeRevealOrigin: Offset = Offset.Unspecified

fun setAppTheme(newTheme: AppTheme, revealOrigin: Offset = Offset.Unspecified) {
    if (newTheme == appTheme) return
    lastAppTheme = appTheme
    appThemeRevealOrigin = revealOrigin
    appTheme = newTheme
}

@Composable
private fun BackgroundThemeContent(
    theme: BackgroundTheme,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    when (theme) {
        is BackgroundTheme.Mesh -> MeshGradientBackground(theme)

        is BackgroundTheme.Solid -> Box(modifier.background(theme.color))

        is BackgroundTheme.SolidStroke -> Box(
            modifier.drawBehind {
                drawRect(
                    brush = Brush.stripes(
                        stripes = theme.stripes,
                        width = theme.width,
                        angle = theme.angle
                    )
                )
            }
        )

        is BackgroundTheme.ArrowStroke -> {
            ArrowStrokeBackground(theme, modifier)
        }

        is BackgroundTheme.Waves -> SolidWavesBackground(
            waves = theme.waves, background = theme.background, modifier = modifier
        )

        is BackgroundTheme.StarsSky -> Box(modifier) {
            MovingStarsBackground(modifier = Modifier.fillMaxSize(), background = theme.color)
            Light(startY = 0f, modifier = Modifier.fillMaxSize(), halfTubeWidth = .3f)
        }

        is BackgroundTheme.RepeatedRectangle -> Box(modifier) {
            RoundedRectGridBackground(
                baseColor = theme.baseColor,
                glowColor = theme.glowColor,
                backgroundColor = theme.backgroundColor,
                cellWidth = theme.cellWidth,
                cellHeight = theme.cellHeight,
                rectWidth = theme.rectWidth,
                rectHeight = theme.rectHeight,
                cornerRadius = theme.cornerRadius,
                glowRadius = theme.glowRadius,
                dotCount = theme.dotCount,
                directionDegrees = theme.directionDegrees,
            )
        }

        is BackgroundTheme.Hexagon -> {
            HexagonBackground(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = theme.backgroundColor,
                hexRadius = theme.hexRadius,
                borderColor = theme.borderColor,
                strokeWidth = theme.strokeWidth
            )
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Background(
                 content: @Composable (appTheme: AppTheme) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = { offset ->
                    increment(offset)
                })
            }
    ) {
        val current = appTheme
        var containerSize by remember { mutableStateOf(IntSize.Zero) }

        val revealSize = remember(current) {
            Animatable(if (lastAppTheme == current) 1f else 0f)
        }
        val revealOrigin = remember(current) {
            if (appThemeRevealOrigin.isSpecified) appThemeRevealOrigin
            else Offset(containerSize.width / 2f, containerSize.height / 2f)
        }

        LaunchedEffect(current) {
            revealSize.animateTo(1f, animationSpec = tween(1150))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { containerSize = it }
        ) {
            // 1. Collect themes that need to be drawn in correct Z-order (bottom to top)
            val themesToRender = mutableListOf<AppTheme>()
            if (revealSize.value < 1f && lastAppTheme != current) {
                themesToRender.add(lastAppTheme) // Draw old theme underneath
            }
            themesToRender.add(current) // Draw new theme on top

            // 2. Render each layer with its own theme snapshot — bg + content both clipped together
            for (theme in themesToRender) {
                key(theme) {
                    val isRevealingTopLayer = (theme == current && revealSize.value < 1f)

                    val clipModifier = if (isRevealingTopLayer) {
                        Modifier.clip(CirclePath(revealSize.value, revealOrigin))
                    } else {
                        Modifier
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .then(clipModifier)
                    ) {
                        BackgroundThemeContent(theme.backgroundTheme)
                        content(theme)
                    }
                }
            }
        }

    }
}


class CirclePath(private val progress: Float, private val origin: Offset = Offset(0f, 0f)) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val maxRadius = sqrt(size.width * size.width + size.height * size.height)
        val radius = maxRadius * progress

        return Outline.Generic(
            Path().apply {
                addOval(
                    Rect(
                        center = origin,
                        radius = radius,
                    )
                )
            }
        )
    }
}

@Composable
private fun Content(isFullscreen: Boolean = false,

                    onToggleFullscreen: (() -> Unit)? = null,currentTheme: AppTheme) {

    val timerRepository = remember { TimerRepository(clock = kotlin.time.Clock.System) }

    val timeLeft = TimerEngine.timeLeft
    val isRunning = TimerEngine.isRunning
    val mode = TimerEngine.mode
    val completedSessions = TimerEngine.completedSessions
    var isSettingsOpen by remember { mutableStateOf(false) }

    val textColor = when (val textTheme = currentTheme.timerTextTheme) {
        is TimerTextTheme.Solid -> textTheme.color
    }
    val containerColor = if(currentTheme.isDark) Color.White else Color.Black
    val contentColor = if (currentTheme.isDark) Color.Black else Color.White
    val pausedColor = Color.Yellow

    val modeLabel = when (mode) {
        TimerMode.FOCUS -> "Focus"
        TimerMode.SHORT_BREAK -> "Short Break"
        TimerMode.LONG_BREAK -> "Long Break"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().safeContentPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            currentTheme.name?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }

            // Mode label
            Text(
                text = modeLabel,
                style = MaterialTheme.typography.titleLarge,
                color = textColor.copy(alpha = 0.9f),
            )


            Spacer(modifier = Modifier.height(24.dp))

            Box(contentAlignment = Alignment.Center) {
                when (val theme = currentTheme.surroundedTextTheme) {
                    is SurroundedTextTheme.MovingTextTheme -> {
                        SurroundingText(theme)

                    }

                    SurroundedTextTheme.None -> {}
                }
                TimerRing(
                    timeLeft = timeLeft,
                    timerShape = currentTheme.timeShape,
                    timerTextTheme = currentTheme.timerTextTheme,
                    totalTime = timerRepository.getDurationForMode(mode),
                    modifier = Modifier.width(280.dp).height(280.dp)
                )

            }
            Spacer(modifier = Modifier.height(24.dp))

            SessionIndicator(
                completedSessions = completedSessions,
                totalSessions = timerRepository.longBreakInterval,
                mode = mode,
                activeColor = Color.Green.copy(alpha = 0.6f),
                inactiveColor = textColor.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledIconButton(
                    onClick = { TimerEngine.reset() },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = containerColor,
                        contentColor =contentColor
                    ),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.refresh),
                        contentDescription = "Reset"
                    )
                }

                FilledIconButton(
                    onClick = { if (isRunning) TimerEngine.pause() else TimerEngine.start() },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (!isRunning) containerColor else pausedColor,
                        contentColor =  contentColor
                    ),
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            if (isRunning) Res.drawable.pause else Res.drawable.play_arrow_fill
                        ),
                        contentDescription = if (isRunning) "Stop" else "Start"
                    )
                }

                FilledIconButton(
                    onClick = { TimerEngine.skip() },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = containerColor,
                        contentColor =contentColor
                    ),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.skip_next),
                        contentDescription = "Skip Next"
                    )
                }
            }
        }




        TextButton(
            onClick = { isSettingsOpen = true },
            modifier = Modifier.align(Alignment.TopEnd).padding(end = 4.dp).safeContentPadding()
        ) {
            Text(
                text = "Settings",
                color = when (val textTheme = currentTheme.timerTextTheme) {
                    is TimerTextTheme.Solid -> textTheme.color
                }.copy(alpha = 0.8f)
            )
        }
        if (onToggleFullscreen != null) {
            IconButton(onClick = onToggleFullscreen) {
                Icon(
                    painter = painterResource(
                        if (isFullscreen) Res.drawable.fullscreen_exit else Res.drawable.fullscreen_fill
                    ),
                    tint = containerColor,
                    contentDescription = if (isFullscreen) "Exit Fullscreen" else "Fullscreen",

                )
            }
        }

        if (isSettingsOpen) {
            Box(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                detectTapGestures(onTap = { isSettingsOpen = false })
            })
        }

        SettingsDrawer(
            isOpen = isSettingsOpen,
            timerRepository = timerRepository,
            onSettingsChanged = {
                if (!isRunning) {
                    TimerEngine.reset()
                }
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

private fun increment(offset: Offset = Offset.Unspecified) {
    i++
    i %= (AppThemePresets.all.size)
    repo.themeIndex = i
    setAppTheme(AppThemePresets.all[i], offset)
}






