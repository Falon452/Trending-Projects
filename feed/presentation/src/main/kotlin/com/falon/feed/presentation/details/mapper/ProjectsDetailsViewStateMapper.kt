package com.falon.feed.presentation.details.mapper

import com.falon.feed.presentation.details.model.ProjectDetailsState
import com.falon.feed.presentation.details.model.ProjectDetailsViewState
import javax.inject.Inject

class ProjectsDetailsViewStateMapper @Inject constructor() {

    fun from(state: ProjectDetailsState): ProjectDetailsViewState = with(state) {
        ProjectDetailsViewState(
            starsResId = starsResId,
            selectedProject = selectedProject,
            readmeContent = readmeContent ?: "",
            isReadmeErrorVisible = loadingReadmeFailed,
        )
    }
}
