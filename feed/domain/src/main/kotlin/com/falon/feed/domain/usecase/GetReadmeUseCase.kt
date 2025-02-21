package com.falon.feed.domain.usecase

import com.falon.feed.domain.repository.TrendingProjectsRepository
import javax.inject.Inject

class GetReadmeUseCase @Inject constructor(
    private val trendingProjectsRepository: TrendingProjectsRepository,
) {

    suspend fun execute(owner: String, repo: String): String? =
        trendingProjectsRepository.getReadmeContent(
            owner = owner,
            repo = repo,
        )
}
