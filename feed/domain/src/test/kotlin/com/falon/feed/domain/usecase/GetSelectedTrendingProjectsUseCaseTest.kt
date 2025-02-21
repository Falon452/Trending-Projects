package com.falon.feed.domain.usecase

import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.domain.repository.TrendingProjectsRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetSelectedTrendingProjectsUseCaseTest {

    private val mockTrendingProjectsRepository = mockk<TrendingProjectsRepository>()
    private val getSelectedTrendingProjectsUseCase =
        GetSelectedTrendingProjectsUseCase(mockTrendingProjectsRepository)

    @Test
    fun `WHEN execute is called with a valid selectedId, THEN it returns the corresponding trending project`() {
        val selectedId = "1"
        val mockProject = mockk<TrendingProject>()

        every { mockTrendingProjectsRepository.getSelectedProject(selectedId) } returns mockProject

        val result = getSelectedTrendingProjectsUseCase.execute(selectedId)

        assertEquals(mockProject, result)
    }

    @Test
    fun `WHEN execute is called with an invalid selectedId, THEN it returns null`() {
        val selectedId = "invalidId"

        every { mockTrendingProjectsRepository.getSelectedProject(selectedId) } returns null

        val result = getSelectedTrendingProjectsUseCase.execute(selectedId)

        assertNull(result)
    }
}
