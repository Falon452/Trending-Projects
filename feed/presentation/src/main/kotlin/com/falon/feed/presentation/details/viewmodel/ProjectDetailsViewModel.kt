package com.falon.feed.presentation.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falon.feed.domain.usecase.ClearSelectedTrendingProjectsUseCase
import com.falon.feed.domain.usecase.GetReadmeUseCase
import com.falon.feed.presentation.details.factory.ProjectDetailsStateFactory
import com.falon.feed.presentation.details.model.ProjectDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val getReadmeUseCase: GetReadmeUseCase,
    private val clearSelectedTrendingProjectsUseCase: ClearSelectedTrendingProjectsUseCase,
    stateFactory: ProjectDetailsStateFactory,
) : ViewModel() {

    private val _projectDetails = MutableStateFlow(stateFactory.create())
    val projectDetails: StateFlow<ProjectDetailsState> = _projectDetails.asStateFlow()

    init {
        loadReadMe()
    }

    private fun loadReadMe() {
        viewModelScope.launch {
            val selectedProject = _projectDetails.value.selectedProject
            val readme = getReadmeUseCase.execute(
                owner = selectedProject.ownerLogin,
                repo = selectedProject.repositoryName,
            )
            _projectDetails.value = _projectDetails.value.copy(readmeContent = readme)
        }
    }

    fun onExitProjectDetails() {
        clearSelectedTrendingProjectsUseCase.execute()
    }

    companion object {

        const val STAR_RESOURCE_ARG = "starsResource"
    }
}
