package com.falon.feed.data.repository

import android.util.Log
import com.falon.feed.data.datasource.GitHubApi
import com.falon.feed.data.datasource.MemoryCache
import com.falon.feed.data.mapper.TrendingProjectMapper
import com.falon.feed.data.model.ReadmeResponse
import com.falon.feed.domain.model.TrendingProject
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class TrendingProjectRepositoryImplTest {

    private val mockGitHubApi = mockk<GitHubApi>()
    private val mockTrendingProjectMapper = mockk<TrendingProjectMapper>()
    private val memoryCache = MemoryCache
    private val testTrendingProject = TrendingProject(
        id = "id",
        ownerLogin = "owner",
        repositoryName = "repo",
        htmlUrl = "http://repo.url",
        stars = 100,
        description = "A trending project",
        ownerAvatarUrl = "http://avatar.url"
    )

    @BeforeEach
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    private val testOwner = "owner"
    private val testRepo = "repo"

    @Test
    fun `WHEN readmeResponse content is null, THEN it returns null`() = runTest {
        val readmeResponse = ReadmeResponse(content = null, encoding = "base64")
        coEvery { mockGitHubApi.getReadme(testOwner, testRepo) } returns readmeResponse

        val result = createRepository().getReadmeContent(testOwner, testRepo)

        assertNull(result)
    }

    @Test
    fun `WHEN readmeResponse content exceeds 1000 lines, THEN only the first 1000 lines are decoded`() =
        runTest {
            val longContent = "dGVzdA==\n".repeat(1500)
            val readmeResponse = ReadmeResponse(content = longContent, encoding = "base64")
            coEvery { mockGitHubApi.getReadme(testOwner, testRepo) } returns readmeResponse

            val result = createRepository().getReadmeContent(testOwner, testRepo)

            assertEquals("test".repeat(1000), result)
            verify(exactly = 0) { Log.e(any(), any()) }
        }

    @Test
    fun `WHEN readmeResponse encoding is other than base64, THEN it logs an error and returns null`() =
        runTest {
            val readmeResponse =
                ReadmeResponse(content = "encoded_content", encoding = "unsupported")
            coEvery { mockGitHubApi.getReadme(testOwner, testRepo) } returns readmeResponse

            val result = createRepository().getReadmeContent(testOwner, testRepo)

            assertNull(result)
            verify { Log.e(TAG, "Unsupported encoding: unsupported") }
        }

    @Test
    fun `WHEN readmeResponse fails, THEN it logs an error and returns null`() = runTest {
        val exceptionMessage = "Network error"
        val exception = Exception(exceptionMessage)
        coEvery { mockGitHubApi.getReadme(testOwner, testRepo) } throws exception

        val result = createRepository().getReadmeContent(testOwner, testRepo)

        assertNull(result)
        verify { Log.e(TAG, "Error fetching README: $exceptionMessage", exception) }
    }

    @Test
    fun `WHEN saveSelected is called, THEN the project is saved to memory cache`() {
        val trendingProjectRepository = createRepository()

        trendingProjectRepository.saveSelected(testTrendingProject)

        assertEquals(testTrendingProject, memoryCache.selectedTrendingProject)
    }

    @Test
    fun `WHEN clearSelectedProject is called, THEN the selected project is cleared from memory cache`() {
        val trendingProjectRepository = createRepository()

        trendingProjectRepository.clearSelectedProject()

        assertNull(memoryCache.selectedTrendingProject)
    }

    @Test
    fun `WHEN getSelectedProject is called with matching ID, THEN it returns the selected project`() {
        val trendingProjectRepository = createRepository()
        memoryCache.selectedTrendingProject = testTrendingProject

        val result = trendingProjectRepository.getSelectedProject(testTrendingProject.id)

        assertEquals(testTrendingProject, result)
    }

    @Test
    fun `WHEN getSelectedProject is called with non-matching ID, THEN it returns null`() {
        val trendingProjectRepository = createRepository()
        memoryCache.selectedTrendingProject = testTrendingProject

        val result = trendingProjectRepository.getSelectedProject("differentId")

        assertNull(result)
    }

    private fun createRepository(
        gitHubApi: GitHubApi = mockGitHubApi,
        trendingProjectMapper: TrendingProjectMapper = mockTrendingProjectMapper,
        memoryCache: MemoryCache = this@TrendingProjectRepositoryImplTest.memoryCache
    ): TrendingProjectRepositoryImpl = TrendingProjectRepositoryImpl(
        gitHubApi = gitHubApi,
        trendingProjectMapper = trendingProjectMapper,
        memoryCache = memoryCache
    )

    private companion object {

        const val TAG = "TrendingProjectRepositoryImpl"
    }
}
