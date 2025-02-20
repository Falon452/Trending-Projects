package com.falon.feed.presentation.details.model

import com.falon.feed.domain.model.TrendingProject

data class ProjectDetailsViewState(
    val starsResId: Int,
    val selectedProject: TrendingProject,
    val isReadmeErrorVisible: Boolean,
    val readmeContent: String,
)
