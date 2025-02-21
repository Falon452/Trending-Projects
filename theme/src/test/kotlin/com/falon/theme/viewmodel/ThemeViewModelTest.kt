package com.falon.theme.viewmodel

import com.falon.theme.ThemePreferences
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class ThemeViewModelTest {

    private val mockThemePreferences = mockk<ThemePreferences>()
    private val ioDispatcher: CoroutineDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() = runTest {
        Dispatchers.setMain(ioDispatcher)
    }

    @Test
    fun `test isDarkTheme value`() = runTest {
        val isDarkModeFlow = MutableStateFlow(false)

        coEvery { mockThemePreferences.observeIsDarkMode() } returns isDarkModeFlow

        val themeViewModel = ThemeViewModel(mockThemePreferences, Dispatchers.IO)
        var collectedIsDarkMode = false
        val job = launch {
            themeViewModel.isDarkTheme.collect { value ->
                collectedIsDarkMode = value
            }
        }

        advanceUntilIdle()
        assertFalse(collectedIsDarkMode)

        isDarkModeFlow.update { true }

        advanceUntilIdle()
        assertTrue(collectedIsDarkMode)
        job.cancel()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
