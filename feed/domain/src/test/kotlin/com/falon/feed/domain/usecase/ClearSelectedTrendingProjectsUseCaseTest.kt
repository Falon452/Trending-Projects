package com.falon.feed.domain.usecase

import com.falon.feed.domain.repository.TrendingProjectsRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class ClearSelectedTrendingProjectsUseCaseTest {

    private val mockTrendingProjectsRepository =
        mockk<TrendingProjectsRepository>(relaxUnitFun = true)
    private val useCase = ClearSelectedTrendingProjectsUseCase(mockTrendingProjectsRepository)

    @Test
    fun `WHEN execute is called, THEN clearSelectedProject is invoked on repository`() {
        useCase.execute()

        verify(exactly = 1) { mockTrendingProjectsRepository.clearSelectedProject() }
    }
}
