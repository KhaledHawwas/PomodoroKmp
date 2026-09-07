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
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import pomodorokmp.shared.generated.resources.Res
import pomodorokmp.shared.generated.resources.pause
import pomodorokmp.shared.generated.resources.photo_prints
import pomodorokmp.shared.generated.resources.play_arrow_fill
import pomodorokmp.shared.generated.resources.refresh
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.seconds

@Composable
fun App() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFF121212)
        )
    ) {

        Background({ Content(it) })
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
    val name:String?= null
)
var i by mutableStateOf(0)

var appTheme by mutableStateOf(
     AppThemePresets.all[i]
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
                baseColor=theme.baseColor,
                        glowColor=theme.glowColor,
                        backgroundColor=theme.backgroundColor,
                        cellWidth=theme.cellWidth,
                        cellHeight=theme.cellHeight,
                        rectWidth=theme.rectWidth,
                        rectHeight=theme.rectHeight,
                        cornerRadius=theme.cornerRadius,
                        glowRadius=theme.glowRadius,
                        dotCount=theme.dotCount,
                        directionDegrees=theme.directionDegrees,
            )
        }
        is BackgroundTheme.Hexagon->{
            HexagonBackground(modifier= Modifier.fillMaxSize(),
                backgroundColor= theme.backgroundColor,
                hexRadius = theme.hexRadius,
                borderColor= theme.borderColor,
                strokeWidth= theme.strokeWidth
            )
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
 fun Background(content: @Composable (appTheme: AppTheme) -> Unit) {
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
private fun Content(currentTheme: AppTheme) {

    val timerRepository = remember { TimerRepository(clock = kotlin.time.Clock.System) }

    var timerState by remember { mutableStateOf(timerRepository.loadTimerState()) }
    var isSettingsOpen by remember { mutableStateOf(false) }

    LaunchedEffect(timerState) {
        timerRepository.saveTimerState(timerState)
    }
    
    LaunchedEffect(timerState.isRunning) {
        if (timerState.isRunning) {
            while (timerState.timeLeft > 0) {
                delay(1.seconds)
                timerState = timerState.copy(timeLeft = timerState.timeLeft - 1)
            }
            timerState = timerRepository.nextState(timerState)
        }
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
                color = when (val textTheme = currentTheme.timerTextTheme) {
                    is TimerTextTheme.Solid -> textTheme.color
                }.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }


        Box(contentAlignment = Alignment.Center) {
            when (val theme = currentTheme.surroundedTextTheme) {
                is SurroundedTextTheme.MovingTextTheme -> {
                    SurroundingText(theme)

                }

                SurroundedTextTheme.None -> {}
            }
            TimerRing(
                timeLeft = timerState.timeLeft,
                timerShape = currentTheme.timeShape,
                timerTextTheme = currentTheme.timerTextTheme,
                totalTime = timerRepository.getDurationForMode(timerState.mode),
                modifier = Modifier.width(280.dp).height(280.dp)
            )

        }

        Spacer(modifier = Modifier.height(48.dp))

        when (val actionButtonTheme = currentTheme.actionButtonTheme) {
           is  ActionButtonTheme.Solid -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { timerState = timerState.copy(isRunning = !timerState.isRunning) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (timerState.isRunning) actionButtonTheme.stopContainerColor else actionButtonTheme.startContainerColor,
                            contentColor = if (timerState.isRunning) actionButtonTheme.stopContentColor else actionButtonTheme.startContentColor
                        )
                    ) {
                        Text(if (timerState.isRunning) "Stop" else "Start")
                    }


                    Shaker(text = "Shake Me", borderColor = actionButtonTheme.resetContentColor, color = actionButtonTheme.contentColor) {
                        increment()
                    }
                    OutlinedButton(
                        onClick = {
                            timerState = timerState.copy(
                                isRunning = false,
                                timeLeft = timerRepository.getDurationForMode(timerState.mode)
                            )
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = actionButtonTheme.resetContentColor
                        )
                    ) {
                        Text("Reset")
                    }
                }
            }
            is ActionButtonTheme.Gradient -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (timerState.isRunning) actionButtonTheme.stopGradient else actionButtonTheme.startGradient)
                            .clickable { timerState = timerState.copy(isRunning = !timerState.isRunning) }
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = if (timerState.isRunning) "Stop" else "Start",
                            color = actionButtonTheme.contentColor
                        )
                    }

                    Shaker(text = "Shake Me", borderColor = actionButtonTheme.resetContentColor, color = actionButtonTheme.contentColor) {
                        increment()
                    }

                    OutlinedButton(
                        onClick = { 
                            timerState = timerState.copy(
                                isRunning = false,
                                timeLeft = timerRepository.getDurationForMode(timerState.mode)
                            )
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = actionButtonTheme.resetContentColor
                        )
                    ) {
                        Text("Reset")
                    }
                }
            }
            is ActionButtonTheme.IconOnly -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledIconButton(
                        onClick = { timerState = timerState.copy(isRunning = !timerState.isRunning) },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = if (timerState.isRunning) actionButtonTheme.stopContainerColor else actionButtonTheme.startContainerColor,
                            contentColor = if (timerState.isRunning) actionButtonTheme.stopContentColor else actionButtonTheme.startContentColor
                        ),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                if (timerState.isRunning) Res.drawable.pause else Res.drawable.play_arrow_fill
                            ),
                            contentDescription = if (timerState.isRunning) "Stop" else "Start"
                        )
                    }

                    Shaker( borderColor = actionButtonTheme.resetContentColor,icon =painterResource(Res.drawable.photo_prints), color = actionButtonTheme.contentColor) {
                        increment()
                    }

                    IconButton(
                        onClick = { 
                            timerState = timerState.copy(
                                isRunning = false,
                                timeLeft = timerRepository.getDurationForMode(timerState.mode)
                            )
                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.refresh),
                            contentDescription = "Reset",
                            tint = actionButtonTheme.resetContentColor
                        )
                    }
                }
            }
            is ActionButtonTheme.Outlined -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { timerState = timerState.copy(isRunning = !timerState.isRunning) },
                        border = BorderStroke(
                            1.5.dp,
                            if (timerState.isRunning) actionButtonTheme.stopBorderColor else actionButtonTheme.startBorderColor
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (timerState.isRunning) actionButtonTheme.stopContentColor else actionButtonTheme.startContentColor
                        )
                    ) {
                        Text(if (timerState.isRunning) "Stop" else "Start")
                    }

                    Shaker(text = "Shake Me", color = actionButtonTheme.contentColor) {
                        increment()
                    }

                    TextButton(
                        onClick = { 
                            timerState = timerState.copy(
                                isRunning = false,
                                timeLeft = timerRepository.getDurationForMode(timerState.mode)
                            )
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = actionButtonTheme.resetContentColor
                        )
                    ) {
                        Text("Reset")
                    }
                }
            }

            }
        }

        TextButton(
            onClick = { isSettingsOpen = true },
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp).safeContentPadding()
        ) {
            Text(
                text = "Settings",
                color = when (val textTheme = currentTheme.timerTextTheme) {
                    is TimerTextTheme.Solid -> textTheme.color
                }.copy(alpha = 0.7f)
            )
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
                if (!timerState.isRunning) {
                    timerState = timerState.copy(timeLeft = timerRepository.getDurationForMode(timerState.mode))
                }
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

private fun increment(offset: Offset = Offset.Unspecified) {
    i++
    i %= (AppThemePresets.all.size)
    setAppTheme(AppThemePresets.all[i], offset)
}






