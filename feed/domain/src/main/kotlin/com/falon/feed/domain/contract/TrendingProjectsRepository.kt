package com.falon.feed.domain.contract

import com.falon.feed.domain.model.TrendingProject
import kotlinx.coroutines.flow.Flow

interface TrendingProjectsRepository {

    fun observe(): Flow<List<TrendingProject>>
}
