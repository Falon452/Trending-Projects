package com.falon.feed.data.mapper

import com.falon.feed.data.model.Repository
import com.falon.feed.domain.model.TrendingProject
import javax.inject.Inject

internal class TrendingProjectMapper @Inject constructor() {

    fun from(repository: Repository): TrendingProject? = with(repository) {
        TrendingProject(
            name = name ?: return null
        )
    }
}
