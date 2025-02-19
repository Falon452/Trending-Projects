package com.falon.feed.data.mapper

import com.falon.feed.data.model.Repository
import com.falon.feed.domain.model.TrendingProject
import javax.inject.Inject

internal class TrendingProjectMapper @Inject constructor() {

    fun from(repository: Repository): TrendingProject? = with(repository) {
        TrendingProject(
            id = id?.toString() ?: return null,
            ownerLogin = owner?.login ?: return null,
            repositoryName = repositoryName ?: return null,
            htmlUrl = htmlUrl ?: return null,
            stars = stars ?: return null,
            description = description ?: return null,
            ownerAvatarUrl = owner.avatarUrl ?: return null,
        )
    }
}
