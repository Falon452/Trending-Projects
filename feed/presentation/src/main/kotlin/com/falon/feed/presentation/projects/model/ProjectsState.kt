package com.falon.feed.presentation.projects.model

import java.time.LocalDateTime

data class ProjectsState(
    val isDarkMode: Boolean = false,
    val showDatePicker: Boolean = false,
    val afterCreatedDate: LocalDateTime = sevenDaysAgo(),
)

private fun sevenDaysAgo(): LocalDateTime = LocalDateTime.now().minusDays(7L)
