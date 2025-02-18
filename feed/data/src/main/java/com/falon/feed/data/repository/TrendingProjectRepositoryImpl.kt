package com.falon.feed.data.repository

import android.util.Log
import com.falon.feed.data.datasource.GitHubApi
import com.falon.feed.data.datasource.GitHubApi.GitHubQuery
import com.falon.feed.domain.contract.TrendingProjectsRepository
import com.falon.feed.domain.model.TrendingProject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

internal class TrendingProjectRepositoryImpl @Inject constructor(
    private val gitHubApi: GitHubApi,
) : TrendingProjectsRepository {

    override fun observe(): Flow<List<TrendingProject>> =
        flow {
            val response = gitHubApi.searchRepositories(
                GitHubQuery(
                    LocalDateTime.now().minusDays(7L),
                    50
                )
            )
            val repositories = response.repositories ?: emptyList()
            val trendingProjects = repositories.map { repo -> TrendingProject(repo.name ?: "") }
            emit(trendingProjects)
        }.catch { e ->
            Log.e(TAG, "Error fetching trending projects: ${e.message}")
            emit(emptyList())
        }

    private companion object {

        const val TAG = "TrendingProjectRepositoryImpl"
    }
}
