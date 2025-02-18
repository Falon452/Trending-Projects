package com.falon.feed.domain.usecase

import androidx.paging.PagingData
import com.falon.feed.domain.contract.TrendingProjectsRepository
import com.falon.feed.domain.model.TrendingProject
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

class ObserveTrendingProjectsUseCase @Inject constructor(
    private val trendingProjectsRepository: TrendingProjectsRepository,
) {

    fun execute(): Flow<PagingData<TrendingProject>> =
        trendingProjectsRepository.observe(
            pageSize = PAGE_SIZE,
            afterCreatedDate = sevenDaysAgo(),
            stars = MINIMUM_STARS,
        )

    private fun sevenDaysAgo(): LocalDateTime = LocalDateTime.now().minusDays(Random.nextLong(7L))

    private companion object {

        const val MINIMUM_STARS = 100
        const val PAGE_SIZE = 50
    }
}
