package com.falon.feed.domain.usecase

import com.falon.feed.domain.contract.TrendingProjectsRepository
import com.falon.feed.domain.model.TrendingProject
import javax.inject.Inject

class SaveSelectedTrendingProjectsUseCase @Inject constructor(
    private val trendingProjectsRepository: TrendingProjectsRepository,
) {

    fun execute(selectedTrendingProject: TrendingProject) {
        trendingProjectsRepository.saveSelected(selectedTrendingProject)
    }
}
