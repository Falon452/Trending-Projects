package com.falon.feed.domain.usecase

import com.falon.feed.domain.contract.TrendingProjectsRepository
import com.falon.feed.domain.model.TrendingProject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTrendingProjectsUseCase @Inject constructor(
    private val trendingProjectsRepository: TrendingProjectsRepository,
) {
    fun execute(): Flow<List<TrendingProject>> =
        trendingProjectsRepository.observe()
}
