package com.falon.feed.presentation.projects.mapper

import com.falon.feed.presentation.projects.model.ProjectsState
import com.falon.feed.presentation.projects.model.ProjectsViewState
import java.time.ZoneId

import javax.inject.Inject

class ProjectsViewStateMapper @Inject constructor() {

    fun from(state: ProjectsState): ProjectsViewState = with(state) {
        val initialSelectedDateMillis = afterCreatedDate
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        ProjectsViewState(
            isDarkMode = isDarkMode,
            showDatePicker = showDatePicker,
            initialSelectedDateMillis = initialSelectedDateMillis
        )
    }
}
