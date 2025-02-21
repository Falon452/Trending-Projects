package com.falon.feed.domain.usecase

import androidx.paging.PagingData
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.domain.repository.TrendingProjectsRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class ObserveTrendingProjectsUseCase @Inject constructor(
    private val trendingProjectsRepository: TrendingProjectsRepository,
) {

    fun execute(afterCreatedDate: LocalDateTime): Flow<PagingData<TrendingProject>> =
        trendingProjectsRepository.observe(
            pageSize = PAGE_SIZE,
            afterCreatedDate = afterCreatedDate,
            stars = MINIMUM_STARS,
        )

    private companion object {

        const val MINIMUM_STARS = 100
        const val PAGE_SIZE = 10
    }
}
