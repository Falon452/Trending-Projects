package com.falon.feed.domain.usecase

import androidx.paging.PagingData
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.domain.repository.TrendingProjectsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class ObserveTrendingProjectsUseCaseTest {

    private val mockTrendingProjectsRepository = mockk<TrendingProjectsRepository>()
    private val observeTrendingProjectsUseCase =
        ObserveTrendingProjectsUseCase(mockTrendingProjectsRepository)

    @Test
    fun `WHEN execute is called, THEN it returns Flow of PagingData with TrendingProjects`() =
        runTest {
            val afterCreatedDate = LocalDateTime.now()
            val mockProject = mockk<TrendingProject>()
            val pagingData = PagingData.from(listOf(mockProject))

            coEvery { mockTrendingProjectsRepository.observe(any(), any(), any()) } returns flowOf(
                pagingData
            )

            val resultFlow = observeTrendingProjectsUseCase.execute(afterCreatedDate)

            val result = mutableListOf<PagingData<TrendingProject>>()
            resultFlow.collect { result.add(it) }

            assertEquals(listOf(pagingData), result)
        }

    @Test
    fun `WHEN execute is called, THEN it applies minimum stars and page size`() = runTest {
        val afterCreatedDate = LocalDateTime.now()
        val mockProject = mockk<TrendingProject>()
        val pagingData = PagingData.from(listOf(mockProject))

        coEvery {
            mockTrendingProjectsRepository.observe(
                10,
                afterCreatedDate,
                100
            )
        } returns flowOf(pagingData)

        val resultFlow = observeTrendingProjectsUseCase.execute(afterCreatedDate)

        val result = mutableListOf<PagingData<TrendingProject>>()
        resultFlow.collect { result.add(it) }

        assertEquals(listOf(pagingData), result)
    }
}
