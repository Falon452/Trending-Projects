package com.falon.feed.presentation.details.viewmodel

import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.domain.usecase.ClearSelectedTrendingProjectsUseCase
import com.falon.feed.domain.usecase.GetReadmeUseCase
import com.falon.feed.presentation.details.factory.ProjectDetailsStateFactory
import com.falon.feed.presentation.details.mapper.ProjectsDetailsViewStateMapper
import com.falon.feed.presentation.details.model.ProjectDetailsState
import com.falon.feed.presentation.details.model.ProjectDetailsViewState
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@FlowPreview
class ProjectDetailsViewModelTest {

    private val mockGetReadmeUseCase = mockk<GetReadmeUseCase>(relaxed = true)
    private val mockClearSelectedTrendingProjectsUseCase =
        mockk<ClearSelectedTrendingProjectsUseCase>(relaxUnitFun = true)
    private val mockStateFactory = mockk<ProjectDetailsStateFactory>()
    private val mockViewStateMapper = mockk<ProjectsDetailsViewStateMapper>()
    private val testDispatcher = StandardTestDispatcher()
    private val ioDispatcher: CoroutineDispatcher = testDispatcher
    private val defaultDispatcher: CoroutineDispatcher = testDispatcher

    private val initialProject = TrendingProject(
        ownerLogin = "owner",
        repositoryName = "repo",
        id = "id",
        ownerAvatarUrl = "ownerAvatarUrl",
        htmlUrl = "htmlUrl",
        stars = 3,
        description = "description",
    )

    val initialState = ProjectDetailsState(
        starsResId = 0,
        selectedProject = initialProject,
        readmeContent = null,
        loadingReadmeFailed = false
    )
    private val initialViewState = ProjectDetailsViewState(
        selectedProject = initialProject,
        readmeContent = "",
        isReadmeErrorVisible = false,
        starsResId = 0,
    )

    @BeforeEach
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)

        every { mockStateFactory.create() } returns ProjectDetailsState(
            starsResId = 0,
            selectedProject = initialProject,
            readmeContent = null,
            loadingReadmeFailed = false
        )

        every { mockViewStateMapper.from(any()) } returns initialViewState
    }

    @Test
    fun `state is mapped to view state`() = runTest {
        val testState = ProjectDetailsState(
            starsResId = 1,
            selectedProject = initialProject,
            readmeContent = "Test Readme Content",
            loadingReadmeFailed = true
        )
        val mockViewState = mockk<ProjectDetailsViewState>()

        every { mockViewStateMapper.from(testState) } returns mockViewState
        every { mockStateFactory.create() } returns testState

        val viewModel = createViewModel()

        assertEquals(mockViewState, viewModel.viewState.value)
    }

    @Test
    fun `WHEN viewmodel is created, THEN loads readme`() = runTest {
        val expectedReadme = "Test Readme Content"
        val owner = "Falon452"
        val repository = "Trending Project"
        val initState = initialState.copy(
            selectedProject = initialState.selectedProject.copy(
                ownerLogin = owner,
                repositoryName = repository,
            )
        )

        coEvery { mockGetReadmeUseCase.execute(owner, repository) } returns expectedReadme
        every { mockStateFactory.create() } returns initState

        val viewModel = createViewModel()
        advanceUntilIdle()

        val expectedState = initState.copy(
            readmeContent = expectedReadme,
            loadingReadmeFailed = false,
        )
        assertEquals(expectedState, viewModel.state)
    }

    @Test
    fun `WHEN on exit project details, THEN clears selected project`() = runTest {
        createViewModel().onExitProjectDetails()
        advanceUntilIdle()

        coVerify { mockClearSelectedTrendingProjectsUseCase.execute() }
    }

    @Test
    fun `WHEN on refresh, THEN reloads readme`() = runTest {
        val initialReadme = "Initial Readme Content"
        val refreshedReadme = "Refreshed Readme Content"
        val owner = "Falon452"
        val repository = "Trending Project"
        val initState = initialState.copy(
            selectedProject = initialState.selectedProject.copy(
                ownerLogin = owner,
                repositoryName = repository,
            )
        )

        coEvery { mockGetReadmeUseCase.execute(owner, repository) } returnsMany listOf(
            initialReadme,
            refreshedReadme
        )
        every { mockStateFactory.create() } returns initState

        val viewModel = createViewModel()
        viewModel.onRefresh()
        advanceUntilIdle()

        val expectedState = initState.copy(
            readmeContent = refreshedReadme,
            loadingReadmeFailed = false
        )
        assertEquals(expectedState, viewModel.state)
    }

    @Test
    fun `WHEN on refresh and getting readme fails, THEN loadingReadmeFailed is true`() = runTest {
        val initialReadme = "Initial Readme Content"
        val owner = "Falon452"
        val repository = "Trending Project"
        val initState = initialState.copy(
            selectedProject = initialState.selectedProject.copy(
                ownerLogin = owner,
                repositoryName = repository,
            )
        )

        coEvery { mockGetReadmeUseCase.execute(owner, repository) } returnsMany listOf(
            initialReadme,
            null,
        )
        every { mockStateFactory.create() } returns initState

        val viewModel = createViewModel()
        viewModel.onRefresh()
        advanceUntilIdle()

        val expectedState = initState.copy(
            readmeContent = null,
            loadingReadmeFailed = true,
        )
        assertEquals(expectedState, viewModel.state)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(
            mockGetReadmeUseCase,
            mockClearSelectedTrendingProjectsUseCase,
            mockStateFactory,
            mockViewStateMapper,
        )
    }

    private fun createViewModel(
        getReadmeUseCase: GetReadmeUseCase = mockGetReadmeUseCase,
        clearSelectedTrendingProjectsUseCase: ClearSelectedTrendingProjectsUseCase = mockClearSelectedTrendingProjectsUseCase,
        stateFactory: ProjectDetailsStateFactory = mockStateFactory,
        viewStateMapper: ProjectsDetailsViewStateMapper = mockViewStateMapper,
        dispatcherIO: CoroutineDispatcher = ioDispatcher,
        dispatcherDefault: CoroutineDispatcher = defaultDispatcher
    ): ProjectDetailsViewModel =
        ProjectDetailsViewModel(
            getReadmeUseCase,
            clearSelectedTrendingProjectsUseCase,
            stateFactory,
            viewStateMapper,
            dispatcherIO,
            dispatcherDefault
        )
}
