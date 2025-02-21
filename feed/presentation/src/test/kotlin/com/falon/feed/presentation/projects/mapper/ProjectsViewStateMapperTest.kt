package com.falon.feed.presentation.projects.mapper

import com.falon.feed.presentation.projects.model.ProjectsState
import com.falon.feed.presentation.projects.model.ProjectsViewState
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.assertEquals

class ProjectsViewStateMapperTest {

    private val mapper = ProjectsViewStateMapper()

    @Test
    fun `WHEN from is called with valid state, THEN it maps to view state`() {
        val state = ProjectsState(
            isDarkMode = true,
            showDatePicker = true,
            afterCreatedDate = LocalDateTime.now().minusDays(7L)
        )

        val viewState = mapper.from(state)

        val expectedInitialSelectedDateMillis = state.afterCreatedDate
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        val expectedViewState = ProjectsViewState(
            isDarkMode = true,
            showDatePicker = true,
            initialSelectedDateMillis = expectedInitialSelectedDateMillis
        )
        assertEquals(expectedViewState, viewState)
    }
}
