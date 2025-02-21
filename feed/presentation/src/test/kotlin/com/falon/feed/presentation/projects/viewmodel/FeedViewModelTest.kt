package com.falon.feed.presentation.projects.viewmodel

import com.falon.feed.domain.usecase.ObserveTrendingProjectsUseCase
import com.falon.feed.domain.usecase.SaveSelectedTrendingProjectsUseCase
import com.falon.feed.presentation.projects.mapper.ProjectsViewStateMapper
import com.falon.feed.presentation.projects.model.ProjectsState
import com.falon.feed.presentation.projects.model.ProjectsViewState
import com.falon.theme.ThemePreferences
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.ZoneId
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@FlowPreview
class FeedViewModelTest {

    private val mockObserveTrendingProjectsUseCase =
        mockk<ObserveTrendingProjectsUseCase>(relaxed = true)
    private val mockSaveSelectedTrendingProjectsUseCase =
        mockk<SaveSelectedTrendingProjectsUseCase>(relaxUnitFun = true)
    private val mockThemePreferences = mockk<ThemePreferences>(relaxed = true)
    private val mockViewStateMapper = mockk<ProjectsViewStateMapper>()
    private val testDispatcher = StandardTestDispatcher()
    private val ioDispatcher: CoroutineDispatcher = testDispatcher
    private val defaultDispatcher: CoroutineDispatcher = testDispatcher

    private val initialViewState = ProjectsViewState(
        isDarkMode = false,
        showDatePicker = false,
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    @BeforeEach
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)

        every { mockViewStateMapper.from(any()) } returns initialViewState
    }

    @Test
    fun `WHEN ViewModel is initialized, THEN clears and reloads trending projects`() = runTest {
        val viewmodel = createViewModel()
        advanceUntilIdle()

        coVerify(exactly = 1) { mockObserveTrendingProjectsUseCase.execute(viewmodel.state.afterCreatedDate) }
    }


    @Test
    fun `state is mapped to view state`() = runTest {
        val mockViewState = mockk<ProjectsViewState>()
        every { mockViewStateMapper.from(any()) } returns mockViewState

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(mockViewState, viewModel.viewState.value)
    }

    @Test
    fun `WHEN theme is toggled, THEN it is saved to preferences`() = runTest {
        createViewModel().onToggleTheme()
        advanceUntilIdle()

        coVerify { mockThemePreferences.saveDarkMode(true) }
    }

    @Test
    fun `WHEN onDateClicked is called, THEN show date picker`() = runTest {
        val viewModel = createViewModel()

        viewModel.onDateClicked()

        assertEquals(true, viewModel.state.showDatePicker)
    }

    @Test
    fun `WHEN onDismissRequest is called, THEN do not show date picker`() = runTest {
        val viewModel = createViewModel()

        viewModel.onDismissRequest()

        assertEquals(false, viewModel.state.showDatePicker)
    }

    @Test
    fun `WHEN onDatePickerConfirmButtonClicked is called, THEN do not show date picker`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.onDatePickerConfirmButtonClicked()

            assertEquals(false, viewModel.state.showDatePicker)
        }

    @Test
    fun `WHEN onDateSelected is called with the same date as previous, THEN does nothing`() =
        runTest {
            val viewModel = createViewModel()
            val initState = ProjectsState()
            val millis = initState.afterCreatedDate
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            viewModel.onDateSelected(millis)
            advanceUntilIdle()

            verify(exactly = 1) { mockObserveTrendingProjectsUseCase.execute(any()) }
        }

    @Test
    fun `WHEN onDateSelected is called and is different than already selected, THEN updates state`() =
        runTest {
            val viewModel = createViewModel()
            val initState = ProjectsState()
            val millis = initState.afterCreatedDate
                .minusDays(10L)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            viewModel.onDateSelected(millis)

            val localDateTime = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            assertEquals(localDateTime, viewModel.state.afterCreatedDate)
        }

    @Test
    fun `WHEN observing dark mode and it changes, THEN state is updated`() = runTest {
        val darkModeFlow = flowOf<Boolean>(true, false, true, false)
        val themePreferences = mockk<ThemePreferences> {
            every { observeIsDarkMode() } returns darkModeFlow
        }
        val viewModel = createViewModel(themePreferences = themePreferences)
        advanceUntilIdle()

        assertEquals(false, viewModel.state.isDarkMode)
    }

    @Test
    fun `WHEN observing dark mode and it changes to true, THEN state is updated`() = runTest {
        val darkModeFlow = flowOf<Boolean>(true, false, true, false, true)
        val themePreferences = mockk<ThemePreferences> {
            every { observeIsDarkMode() } returns darkModeFlow
        }
        val viewModel = createViewModel(themePreferences = themePreferences)
        advanceUntilIdle()

        assertEquals(true, viewModel.state.isDarkMode)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(
            mockObserveTrendingProjectsUseCase,
            mockSaveSelectedTrendingProjectsUseCase,
            mockThemePreferences,
            mockViewStateMapper
        )
    }

    private fun createViewModel(
        observeTrendingProjectsUseCase: ObserveTrendingProjectsUseCase = mockObserveTrendingProjectsUseCase,
        saveSelectedTrendingProjectsUseCase: SaveSelectedTrendingProjectsUseCase = mockSaveSelectedTrendingProjectsUseCase,
        themePreferences: ThemePreferences = mockThemePreferences,
        viewStateMapper: ProjectsViewStateMapper = mockViewStateMapper,
        dispatcherIO: CoroutineDispatcher = ioDispatcher,
        dispatcherDefault: CoroutineDispatcher = defaultDispatcher
    ): FeedViewModel =
        FeedViewModel(
            observeTrendingProjectsUseCase,
            saveSelectedTrendingProjectsUseCase,
            themePreferences,
            viewStateMapper,
            dispatcherIO,
            dispatcherDefault
        )
}
