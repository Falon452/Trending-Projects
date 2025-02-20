package com.falon.feed.presentation.details.factory

import androidx.lifecycle.SavedStateHandle
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.domain.usecase.GetSelectedTrendingProjectsUseCase
import com.falon.feed.presentation.R
import com.falon.feed.presentation.details.model.ProjectDetailsState
import com.falon.feed.presentation.details.viewmodel.ProjectDetailsViewModel.Companion.STAR_RESOURCE_ARG
import javax.inject.Inject

class ProjectDetailsStateFactory @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSelectedTrendingProjectsUseCase: GetSelectedTrendingProjectsUseCase,
) {

    fun create(): ProjectDetailsState =
        ProjectDetailsState(
            starsResId = savedStateHandle[STAR_RESOURCE_ARG] ?: R.drawable.star0,
            selectedProject = getSelectedTrendingProjectsUseCase.execute() ?: emptyProject(),
            readmeContent = null,
            loadingReadmeFailed = false,
        )

    private fun emptyProject(): TrendingProject = TrendingProject(
        id = "",
        ownerLogin = "",
        ownerAvatarUrl = "",
        repositoryName = "",
        htmlUrl = "",
        stars = 0,
        description = "",
    )
}
