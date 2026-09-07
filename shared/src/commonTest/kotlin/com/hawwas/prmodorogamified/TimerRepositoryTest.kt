package com.hawwas.prmodorogamified

import com.russhwolf.settings.MapSettings
import com.hawwas.pomodorokmp.TimerRepository
import com.hawwas.pomodorokmp.TimerState
import com.hawwas.pomodorokmp.TimerMode
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FakeClock(var currentInstant: Instant) : Clock {
    override fun now(): Instant = currentInstant
}

class TimerRepositoryTest {

    @Test
    fun testTimerBackgroundPauseAndResume() {
        val fakeClock = FakeClock(Instant.fromEpochSeconds(1000))
        val settings = MapSettings()
        val repo = TimerRepository(settings, fakeClock)

        val initialState = TimerState(
            timeLeft = 25 * 60,
            isRunning = true,
            mode = TimerMode.FOCUS,
            completedSessions = 0
        )
        repo.saveTimerState(initialState)

        fakeClock.currentInstant = Instant.fromEpochSeconds(1000 + 5 * 60)

        val loadedState = repo.loadTimerState()
        
        assertEquals(20 * 60, loadedState.timeLeft)
        assertTrue(loadedState.isRunning)
        assertEquals(TimerMode.FOCUS, loadedState.mode)
    }

    @Test
    fun testTimerBackgroundElapsedCompleteSession() {
        val fakeClock = FakeClock(Instant.fromEpochSeconds(1000))
        val settings = MapSettings()
        val repo = TimerRepository(settings, fakeClock)

        val initialState = TimerState(
            timeLeft = 25 * 60,
            isRunning = true,
            mode = TimerMode.FOCUS,
            completedSessions = 0
        )
        repo.saveTimerState(initialState)

        fakeClock.currentInstant = Instant.fromEpochSeconds(1000 + 30 * 60)

        val loadedState = repo.loadTimerState()
        
        assertFalse(loadedState.isRunning)
        assertEquals(TimerMode.SHORT_BREAK, loadedState.mode)
        assertEquals(1, loadedState.completedSessions)
        assertEquals(repo.shortBreakDuration, loadedState.timeLeft)
    }

    @Test
    fun testTimerLongBreakAfterFourSessions() {
        val fakeClock = FakeClock(Instant.fromEpochSeconds(1000))
        val settings = MapSettings()
        val repo = TimerRepository(settings, fakeClock)

        val initialState = TimerState(
            timeLeft = 25 * 60,
            isRunning = true,
            mode = TimerMode.FOCUS,
            completedSessions = 3
        )
        repo.saveTimerState(initialState)

        fakeClock.currentInstant = Instant.fromEpochSeconds(1000 + 26 * 60)

        val loadedState = repo.loadTimerState()

        assertEquals(TimerMode.LONG_BREAK, loadedState.mode)
        assertEquals(4, loadedState.completedSessions)
        assertFalse(loadedState.isRunning)
    }
}
