package com.falon.feed.domain.model

data class TrendingProject(
    val id: String,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val repositoryName: String,
    val htmlUrl: String,
    val stars: Int,
    val description: String,
)
