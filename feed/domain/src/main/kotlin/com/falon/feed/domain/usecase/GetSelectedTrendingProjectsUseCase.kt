package com.falon.feed.domain.usecase

import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.domain.repository.TrendingProjectsRepository
import javax.inject.Inject

class GetSelectedTrendingProjectsUseCase @Inject constructor(
    private val trendingProjectsRepository: TrendingProjectsRepository,
) {

    fun execute(selectedId: String): TrendingProject? =
        trendingProjectsRepository.getSelectedProject(selectedId)
}
