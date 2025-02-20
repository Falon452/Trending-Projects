package com.falon.feed.data.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.falon.feed.data.mapper.TrendingProjectMapper
import com.falon.feed.data.model.GitHubQuery
import com.falon.feed.domain.model.TrendingProject

internal class TrendingProjectsPagingSource(
    private val query: GitHubQuery,
    private val perPage: Int,
    private val gitHubApi: GitHubApi,
    private val trendingProjectMapper: TrendingProjectMapper,
) : PagingSource<Int, TrendingProject>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TrendingProject> =
        try {
            val currentPage = params.key ?: FIRST_PAGE
            val response = gitHubApi.searchRepositories(
                query = query,
                page = currentPage,
                perPage = perPage,
            )

            val repositories = response.repositories ?: emptyList()
            val trendingProjects = repositories.mapNotNull(trendingProjectMapper::from)

            LoadResult.Page(
                data = trendingProjects,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (trendingProjects.isEmpty()) null else currentPage + 1,
            )

        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, TrendingProject>): Int? =
        state.anchorPosition

    private companion object {

        const val TAG = "TrendingProjectsPagingSource"
        const val FIRST_PAGE = 1
    }
}
