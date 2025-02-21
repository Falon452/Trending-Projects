package com.falon.feed.presentation.details.mapper

import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.presentation.details.model.ProjectDetailsState
import com.falon.feed.presentation.details.model.ProjectDetailsViewState
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProjectsDetailsViewStateMapperTest {

    private val mapper = ProjectsDetailsViewStateMapper()

    @Test
    fun `WHEN from is called with valid state, THEN it maps to view state`() {
        val selectedProject = TrendingProject(
            id = "1",
            ownerLogin = "owner",
            ownerAvatarUrl = "http://avatar.url",
            repositoryName = "Test Repo",
            htmlUrl = "http://repo.url",
            stars = 100,
            description = "Test description"
        )
        val state = ProjectDetailsState(
            starsResId = 123,
            selectedProject = selectedProject,
            readmeContent = "Readme content",
            loadingReadmeFailed = false
        )


        val viewState = mapper.from(state)

        val expectedViewState = ProjectDetailsViewState(
            starsResId = 123,
            selectedProject = selectedProject,
            isReadmeErrorVisible = false,
            readmeContent = "Readme content"
        )
        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun `WHEN from is called with null readmeContent, THEN it maps empty string for readmeContent`() {
        val selectedProject = TrendingProject(
            id = "1",
            ownerLogin = "owner",
            ownerAvatarUrl = "http://avatar.url",
            repositoryName = "Test Repo",
            htmlUrl = "http://repo.url",
            stars = 100,
            description = "Test description"
        )
        val state = ProjectDetailsState(
            starsResId = 123,
            selectedProject = selectedProject,
            readmeContent = null,
            loadingReadmeFailed = true
        )

        val viewState = mapper.from(state)

        val expectedViewState = ProjectDetailsViewState(
            starsResId = 123,
            selectedProject = selectedProject,
            isReadmeErrorVisible = true,
            readmeContent = ""
        )
        assertEquals(expectedViewState, viewState)
    }
}
