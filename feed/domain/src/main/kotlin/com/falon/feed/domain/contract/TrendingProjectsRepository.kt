package com.falon.feed.domain.contract

import androidx.paging.PagingData
import com.falon.feed.domain.model.TrendingProject
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TrendingProjectsRepository {

    fun observe(
        pageSize: Int,
        afterCreatedDate: LocalDateTime,
        stars: Int
    ): Flow<PagingData<TrendingProject>>

    suspend fun getReadmeContent(
        owner: String,
        repo: String,
    ): String?

    fun saveSelected(project: TrendingProject)
    fun clearSelectedProject()
    fun getSelectedProject(): TrendingProject?
}
