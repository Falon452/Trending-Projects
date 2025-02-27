package com.falon.feed.domain.usecase

import com.falon.feed.domain.repository.TrendingProjectsRepository
import javax.inject.Inject

class ClearSelectedTrendingProjectsUseCase @Inject constructor(
    private val trendingProjectsRepository: TrendingProjectsRepository,
) {

    fun execute() {
        trendingProjectsRepository.clearSelectedProject()
    }
}
