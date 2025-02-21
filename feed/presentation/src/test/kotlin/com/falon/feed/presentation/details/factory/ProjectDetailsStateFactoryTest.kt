package com.falon.feed.presentation.details.factory

import androidx.lifecycle.SavedStateHandle
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.domain.usecase.GetSelectedTrendingProjectsUseCase
import com.falon.feed.presentation.R
import com.falon.feed.presentation.details.model.ProjectDetailsState
import com.falon.feed.presentation.details.viewmodel.ProjectDetailsViewModel.Companion.STAR_RESOURCE_ARG
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProjectDetailsStateFactoryTest {

    private val mockSavedStateHandle = mockk<SavedStateHandle>()
    private val mockGetSelectedTrendingProjectsUseCase = mockk<GetSelectedTrendingProjectsUseCase>()
    private val factory =
        ProjectDetailsStateFactory(mockSavedStateHandle, mockGetSelectedTrendingProjectsUseCase)

    @Test
    fun `WHEN create is called, THEN it returns state with correct values from SavedStateHandle and use case`() {
        val trendingProject = TrendingProject(
            id = "1",
            ownerLogin = "owner",
            ownerAvatarUrl = "http://avatar.url",
            repositoryName = "Test Repo",
            htmlUrl = "http://repo.url",
            stars = 100,
            description = "Test description"
        )

        every { mockSavedStateHandle.get<Int>(STAR_RESOURCE_ARG) } returns R.drawable.star1
        every { mockGetSelectedTrendingProjectsUseCase.execute() } returns trendingProject

        val result = factory.create()

        val expectedState = ProjectDetailsState(
            starsResId = R.drawable.star1,
            selectedProject = trendingProject,
            readmeContent = null,
            loadingReadmeFailed = false
        )

        assertEquals(expectedState, result)
    }

    @Test
    fun `WHEN create is called with no star resource in SavedStateHandle, THEN it uses default star resource`() {
        val trendingProject = TrendingProject(
            id = "1",
            ownerLogin = "owner",
            ownerAvatarUrl = "http://avatar.url",
            repositoryName = "Test Repo",
            htmlUrl = "http://repo.url",
            stars = 100,
            description = "Test description"
        )

        every { mockSavedStateHandle.get<Int>(STAR_RESOURCE_ARG) } returns null
        every { mockGetSelectedTrendingProjectsUseCase.execute() } returns trendingProject

        val result = factory.create()

        val expectedState = ProjectDetailsState(
            starsResId = R.drawable.star0,
            selectedProject = trendingProject,
            readmeContent = null,
            loadingReadmeFailed = false
        )
        assertEquals(expectedState, result)
    }

    @Test
    fun `WHEN create is called with null selected project, THEN it returns empty project`() {
        every { mockSavedStateHandle.get<Int>(STAR_RESOURCE_ARG) } returns R.drawable.star0
        every { mockGetSelectedTrendingProjectsUseCase.execute() } returns null

        val result = factory.create()

        val expectedEmptyProject = TrendingProject(
            id = "",
            ownerLogin = "",
            ownerAvatarUrl = "",
            repositoryName = "",
            htmlUrl = "",
            stars = 0,
            description = "",
        )
        val expectedState = ProjectDetailsState(
            starsResId = R.drawable.star0,
            selectedProject = expectedEmptyProject,
            readmeContent = null,
            loadingReadmeFailed = false
        )
        assertEquals(expectedState, result)
    }
}
