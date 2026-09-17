package com.hawwas.pomodorokmp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import kotlin.time.Clock

object TimerEngine {
    private val clock = Clock.System
    private val repository = TimerRepository(clock = clock)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var tickJob: Job? = null
    private var endTimeUnix: Long = 0L

    private val initial = repository.loadTimerState()

    var timeLeft by mutableStateOf(initial.timeLeft)
        private set
    var isRunning by mutableStateOf(initial.isRunning)
        private set
    var mode by mutableStateOf(initial.mode)
        private set
    var completedSessions by mutableStateOf(initial.completedSessions)
        private set

    init {
        if (isRunning) {
            endTimeUnix = clock.now().epochSeconds + timeLeft
            startTicking()
        }
    }

    fun start() {
        if (isRunning) return
        isRunning = true
        endTimeUnix = clock.now().epochSeconds + timeLeft
        persist()
        startTicking()
    }

    fun pause() {
        if (!isRunning) return
        tickJob?.cancel()
        isRunning = false
        persist()
    }

    fun skip() = advance()

    fun reset() {
        tickJob?.cancel()
        repository.clearTimerState()
        applyState(repository.loadTimerState())
    }

    private fun startTicking() {
        tickJob?.cancel()
        tickJob = scope.launch {
            while (isActive) {
                timeLeft = (endTimeUnix - clock.now().epochSeconds).toInt().coerceAtLeast(0)
                if (timeLeft <= 0) {
                    playBeepSound()
                    advance()
                    break
                }
                delay(1000)
            }
        }
    }

    private fun advance() {
        applyState(repository.nextState(currentState()))
    }

    private fun applyState(state: TimerState) {
        timeLeft = state.timeLeft
        isRunning = state.isRunning
        mode = state.mode
        completedSessions = state.completedSessions
        if (isRunning) {
            endTimeUnix = clock.now().epochSeconds + timeLeft
            startTicking()
        } else {
            tickJob?.cancel()
        }
    }

    private fun currentState() = TimerState(timeLeft, isRunning, mode, completedSessions)

    private fun persist() = repository.saveTimerState(currentState())
}