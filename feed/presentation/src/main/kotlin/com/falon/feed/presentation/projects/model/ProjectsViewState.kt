package com.falon.feed.presentation.projects.model

data class ProjectsViewState(
    val isDarkMode: Boolean,
    val showDatePicker: Boolean,
    val initialSelectedDateMillis: Long,
)
