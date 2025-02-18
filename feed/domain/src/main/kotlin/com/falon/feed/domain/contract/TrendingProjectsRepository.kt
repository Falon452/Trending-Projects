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
}
