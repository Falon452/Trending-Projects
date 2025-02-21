package com.falon.feed.domain.usecase

import com.falon.feed.domain.repository.TrendingProjectsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetReadmeUseCaseTest {

    private val mockTrendingProjectsRepository = mockk<TrendingProjectsRepository>()
    private val getReadmeUseCase = GetReadmeUseCase(mockTrendingProjectsRepository)

    @Test
    fun `WHEN execute is called with valid owner and repo, THEN it returns readme content`() =
        runTest {
            val owner = "owner"
            val repo = "repo"
            val readmeContent = "README content"

            coEvery {
                mockTrendingProjectsRepository.getReadmeContent(
                    owner,
                    repo
                )
            } returns readmeContent

            val result = getReadmeUseCase.execute(owner, repo)

            assertEquals(readmeContent, result)
        }

    @Test
    fun `WHEN execute is called with valid owner and repo, but no readme content exists, THEN it returns null`() =
        runTest {
            val owner = "owner"
            val repo = "repo"

            coEvery { mockTrendingProjectsRepository.getReadmeContent(owner, repo) } returns null

            val result = getReadmeUseCase.execute(owner, repo)

            assertNull(result)
        }

    @Test
    fun `WHEN execute is called with an invalid owner or repo, THEN it returns null`() = runTest {
        val owner = "invalidOwner"
        val repo = "invalidRepo"

        coEvery { mockTrendingProjectsRepository.getReadmeContent(owner, repo) } returns null

        val result = getReadmeUseCase.execute(owner, repo)

        assertNull(result)
    }
}
