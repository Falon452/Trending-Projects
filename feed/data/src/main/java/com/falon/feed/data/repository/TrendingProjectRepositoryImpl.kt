package com.falon.feed.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.falon.feed.data.datasource.GitHubApi
import com.falon.feed.data.datasource.GitHubApi.GitHubQuery
import com.falon.feed.data.datasource.TrendingProjectsPagingSource
import com.falon.feed.data.mapper.TrendingProjectMapper
import com.falon.feed.domain.contract.TrendingProjectsRepository
import com.falon.feed.domain.model.TrendingProject
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

internal class TrendingProjectRepositoryImpl @Inject constructor(
    private val gitHubApi: GitHubApi,
    private val trendingProjectMapper: TrendingProjectMapper,
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
}
