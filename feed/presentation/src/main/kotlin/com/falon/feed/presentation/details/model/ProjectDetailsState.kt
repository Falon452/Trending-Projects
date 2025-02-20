package com.falon.feed.presentation.details.model

import com.falon.feed.domain.model.TrendingProject

data class ProjectDetailsState(
    val starsResId: Int,
    val selectedProject: TrendingProject,
    val readmeContent: String?
)
