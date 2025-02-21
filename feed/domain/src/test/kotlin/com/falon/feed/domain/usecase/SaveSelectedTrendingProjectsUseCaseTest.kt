package com.falon.feed.domain.usecase

import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.domain.repository.TrendingProjectsRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class SaveSelectedTrendingProjectsUseCaseTest {

    private val mockTrendingProjectsRepository = mockk<TrendingProjectsRepository>(relaxed = true)
    private val saveSelectedTrendingProjectsUseCase =
        SaveSelectedTrendingProjectsUseCase(mockTrendingProjectsRepository)

    @Test
    fun `WHEN execute is called, THEN it saves the selected trending project`() {
        val selectedTrendingProject = TrendingProject(
            id = "1",
            ownerLogin = "owner",
            ownerAvatarUrl = "http://avatar.url",
            repositoryName = "Test Repo",
            htmlUrl = "http://repo.url",
            stars = 100,
            description = "Test description"
        )

        saveSelectedTrendingProjectsUseCase.execute(selectedTrendingProject)

        verify(exactly = 1) { mockTrendingProjectsRepository.saveSelected(selectedTrendingProject) }
    }
}
