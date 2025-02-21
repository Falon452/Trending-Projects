package com.falon.feed.data.datasource

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.falon.feed.data.mapper.TrendingProjectMapper
import com.falon.feed.data.model.GitHubQuery
import com.falon.feed.data.model.GitHubSearchResponse
import com.falon.feed.data.model.Repository
import com.falon.feed.domain.model.TrendingProject
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalPagingApi::class)
@ExperimentalCoroutinesApi
class TrendingProjectsPagingSourceTest {

    private val mockGitHubApi = mockk<GitHubApi>()
    private val mockTrendingProjectMapper = mockk<TrendingProjectMapper>()
    private val mockGitHubQuery = mockk<GitHubQuery>()
    private val perPage = 10

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
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `WHEN load is successful, THEN returns LoadResult Page`() = runTest {
        val mockResponse = mockk<GitHubSearchResponse>()
        val repositories = listOf(mockk<Repository>().apply {
            every { mockTrendingProjectMapper.from(any()) } returns testTrendingProject
        })
        every { mockResponse.repositories } returns repositories
        coEvery {
            mockGitHubApi.searchRepositories(
                query = mockGitHubQuery,
                page = 1,
                perPage = perPage
            )
        } returns mockResponse

        val result = createPagingSource().load(LoadParams.Refresh(1, perPage, false))

        assertTrue(result is LoadResult.Page)
        val pageResult = result as LoadResult.Page
        assertEquals(pageResult.data.size, 1)
        assertEquals(pageResult.data[0], testTrendingProject)
        assertNull(pageResult.prevKey)
        assertEquals(pageResult.nextKey, 2)
    }

    @Test
    fun `WHEN repositories are empty, THEN returns empty data with null nextKey`() = runTest {
        val mockResponse = mockk<GitHubSearchResponse>()
        every { mockResponse.repositories } returns emptyList()
        coEvery {
            mockGitHubApi.searchRepositories(
                query = mockGitHubQuery,
                page = 1,
                perPage = perPage
            )
        } returns mockResponse

        val result = createPagingSource().load(LoadParams.Refresh(1, perPage, false))

        assertTrue(result is LoadResult.Page)
        val pageResult = result as LoadResult.Page
        assertTrue(pageResult.data.isEmpty())
        assertNull(pageResult.nextKey)
    }

    @Test
    fun `WHEN load throws an exception, THEN returns LoadResult Error`() = runTest {
        coEvery {
            mockGitHubApi.searchRepositories(
                query = mockGitHubQuery,
                page = 1,
                perPage = perPage
            )
        } throws Exception("API Error")

        val result = createPagingSource().load(LoadParams.Refresh(1, perPage, false))

        assertTrue(result is LoadResult.Error)
        val errorResult = result as LoadResult.Error
        assertTrue(errorResult.throwable is Exception)
    }

    @Test
    fun `WHEN load is called on the first page, THEN prevKey is null`() = runTest {
        val mockResponse = mockk<GitHubSearchResponse>()
        val repositories = listOf(mockk<Repository>().apply {
            every { mockTrendingProjectMapper.from(any()) } returns testTrendingProject
        })
        every { mockResponse.repositories } returns repositories
        coEvery {
            mockGitHubApi.searchRepositories(
                query = mockGitHubQuery,
                page = 1,
                perPage = perPage
            )
        } returns mockResponse

        val result = createPagingSource().load(LoadParams.Refresh(1, perPage, false))

        assertTrue(result is LoadResult.Page)
        val pageResult = result as LoadResult.Page
        assertNull(pageResult.prevKey)
    }

    @Test
    fun `WHEN load is called after the first page, THEN prevKey is not null`() = runTest {
        val mockResponse = mockk<GitHubSearchResponse>()
        val repositories = listOf(mockk<Repository>().apply {
            every { mockTrendingProjectMapper.from(any()) } returns testTrendingProject
        })
        every { mockResponse.repositories } returns repositories
        coEvery {
            mockGitHubApi.searchRepositories(
                query = mockGitHubQuery,
                page = 2,
                perPage = perPage
            )
        } returns mockResponse

        val result = createPagingSource().load(LoadParams.Refresh(2, perPage, false))

        assertTrue(result is LoadResult.Page)
        val pageResult = result as LoadResult.Page
        assertEquals(pageResult.prevKey, 1)
    }

    private fun createPagingSource(): TrendingProjectsPagingSource = TrendingProjectsPagingSource(
        query = mockGitHubQuery,
        perPage = perPage,
        gitHubApi = mockGitHubApi,
        trendingProjectMapper = mockTrendingProjectMapper
    )
}
