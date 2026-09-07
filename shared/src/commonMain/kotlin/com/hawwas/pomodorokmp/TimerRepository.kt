package com.hawwas.pomodorokmp

import com.russhwolf.settings.Settings
import kotlinx.datetime.Clock

enum class TimerMode {
    FOCUS, SHORT_BREAK, LONG_BREAK
}

data class TimerState(
    val timeLeft: Int,
    val isRunning: Boolean,
    val mode: TimerMode,
    val completedSessions: Int
)

class TimerRepository(
    private val settings: Settings = Settings(),
    private val clock: Clock
) {
    companion object {
        const val KEY_TIME_LEFT = "time_left"
        const val KEY_IS_RUNNING = "is_running"
        const val KEY_END_TIME_UNIX = "end_time_unix"
        const val KEY_MODE = "timer_mode"
        const val KEY_COMPLETED_SESSIONS = "completed_sessions"

        const val KEY_FOCUS_DURATION = "focus_duration"
        const val KEY_SHORT_BREAK_DURATION = "short_break_duration"
        const val KEY_LONG_BREAK_DURATION = "long_break_duration"
        const val KEY_LONG_BREAK_INTERVAL = "long_break_interval"

        const val DEFAULT_FOCUS = 25 * 60
        const val DEFAULT_SHORT_BREAK = 5 * 60
        const val DEFAULT_LONG_BREAK = 15 * 60
        const val DEFAULT_INTERVAL = 4
    }

    var focusDuration: Int
        get() = settings.getInt(KEY_FOCUS_DURATION, DEFAULT_FOCUS)
        set(value) = settings.putInt(KEY_FOCUS_DURATION, value)
        
    var shortBreakDuration: Int
        get() = settings.getInt(KEY_SHORT_BREAK_DURATION, DEFAULT_SHORT_BREAK)
        set(value) = settings.putInt(KEY_SHORT_BREAK_DURATION, value)
        
    var longBreakDuration: Int
        get() = settings.getInt(KEY_LONG_BREAK_DURATION, DEFAULT_LONG_BREAK)
        set(value) = settings.putInt(KEY_LONG_BREAK_DURATION, value)

    var longBreakInterval: Int
        get() = settings.getInt(KEY_LONG_BREAK_INTERVAL, DEFAULT_INTERVAL)
        set(value) = settings.putInt(KEY_LONG_BREAK_INTERVAL, value)

    fun saveTimerState(state: TimerState) {
        settings.putBoolean(KEY_IS_RUNNING, state.isRunning)
        settings.putString(KEY_MODE, state.mode.name)
        settings.putInt(KEY_COMPLETED_SESSIONS, state.completedSessions)
        
        if (state.isRunning) {
            val endTimeUnix = clock.now().epochSeconds + state.timeLeft
            settings.putLong(KEY_END_TIME_UNIX, endTimeUnix)
        } else {
            settings.putInt(KEY_TIME_LEFT, state.timeLeft)
        }
    }

    fun loadTimerState(): TimerState {
        val isRunning = settings.getBoolean(KEY_IS_RUNNING, false)
        val modeStr = settings.getString(KEY_MODE, TimerMode.FOCUS.name)
        val mode = try { TimerMode.valueOf(modeStr) } catch (e: Exception) { TimerMode.FOCUS }
        val completedSessions = settings.getInt(KEY_COMPLETED_SESSIONS, 0)
        val defaultTimeForMode = getDurationForMode(mode)

        if (isRunning) {
            val endTimeUnix = settings.getLong(KEY_END_TIME_UNIX, 0L)
            val currentTimeUnix = clock.now().epochSeconds
            val timeLeft = (endTimeUnix - currentTimeUnix).toInt()

            if (timeLeft <= 0) {
                // Time elapsed in background! Move to next state immediately.
                val nextState = calculateNextState(mode, completedSessions)
                saveTimerState(nextState)
                return nextState
            } else {
                return TimerState(timeLeft, true, mode, completedSessions)
            }
        } else {
            val timeLeft = settings.getInt(KEY_TIME_LEFT, defaultTimeForMode)
            return TimerState(timeLeft, false, mode, completedSessions)
        }
    }

    fun nextState(currentState: TimerState): TimerState {
        val next = calculateNextState(currentState.mode, currentState.completedSessions)
        saveTimerState(next)
        return next
    }

    private fun calculateNextState(currentMode: TimerMode, currentCompleted: Int): TimerState {
        var nextMode = currentMode
        var nextCompleted = currentCompleted

        if (currentMode == TimerMode.FOCUS) {
            nextCompleted += 1
            if (nextCompleted % longBreakInterval == 0) {
                nextMode = TimerMode.LONG_BREAK
            } else {
                nextMode = TimerMode.SHORT_BREAK
            }
        } else {
            nextMode = TimerMode.FOCUS
        }

        return TimerState(
            timeLeft = getDurationForMode(nextMode),
            isRunning = false,
            mode = nextMode,
            completedSessions = nextCompleted
        )
    }

    fun getDurationForMode(mode: TimerMode): Int {
        return when(mode) {
            TimerMode.FOCUS -> focusDuration
            TimerMode.SHORT_BREAK -> shortBreakDuration
            TimerMode.LONG_BREAK -> longBreakDuration
        }
    }

    fun clear() {
        settings.clear()
    }
}
