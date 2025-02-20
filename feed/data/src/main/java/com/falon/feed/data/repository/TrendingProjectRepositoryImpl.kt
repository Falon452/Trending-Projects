package com.falon.feed.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.falon.feed.data.datasource.GitHubApi
import com.falon.feed.data.datasource.GitHubApi.GitHubQuery
import com.falon.feed.data.datasource.MemoryCache
import com.falon.feed.data.datasource.TrendingProjectsPagingSource
import com.falon.feed.data.mapper.TrendingProjectMapper
import com.falon.feed.domain.contract.TrendingProjectsRepository
import com.falon.feed.domain.model.TrendingProject
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal class TrendingProjectRepositoryImpl @Inject constructor(
    private val gitHubApi: GitHubApi,
    private val trendingProjectMapper: TrendingProjectMapper,
    private val memoryCache: MemoryCache,
) : TrendingProjectsRepository {

    override fun observe(
        pageSize: Int,
        afterCreatedDate: LocalDateTime,
        stars: Int
    ): Flow<PagingData<TrendingProject>> =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                TrendingProjectsPagingSource(
                    query = GitHubQuery(
                        afterCreatedDate,
                        stars,
                    ),
                    perPage = pageSize,
                    gitHubApi = gitHubApi,
                    trendingProjectMapper = trendingProjectMapper,
                )
            }
        ).flow

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun getReadmeContent(owner: String, repo: String): String? {
        return try {
            val readmeResponse = gitHubApi.getReadme(owner, repo)
            val stringBuilder = StringBuilder()
            when (readmeResponse.encoding) {
                "base64" -> {
                    readmeResponse.content.split("\n").asSequence().take(MAX_LINES).forEach {
                        val decodedBytes = Base64.decode(it.toByteArray())
                        val utf8 = String(decodedBytes, Charsets.UTF_8)
                        stringBuilder.append(utf8)
                    }
                }

                else -> {
                    Log.e(TAG, "Unsupported encoding: ${readmeResponse.encoding}")
                    return null
                }
            }
            stringBuilder.toString()
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching README: ${e.message}", e)
            null
        }
    }

    override fun saveSelected(project: TrendingProject) {
        memoryCache.selectedTrendingProject = project
    }

    override fun clearSelectedProject() {
        memoryCache.selectedTrendingProject = null
    }

    override fun getSelectedProject(): TrendingProject? =
        memoryCache.selectedTrendingProject

    private companion object {

        const val MAX_LINES = 1000
        const val TAG = "TrendingProjectRepositoryImpl"
    }
}
