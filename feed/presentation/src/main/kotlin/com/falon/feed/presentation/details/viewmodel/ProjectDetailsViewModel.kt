package com.falon.feed.presentation.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falon.feed.domain.usecase.ClearSelectedTrendingProjectsUseCase
import com.falon.feed.domain.usecase.GetReadmeUseCase
import com.falon.feed.presentation.details.factory.ProjectDetailsStateFactory
import com.falon.feed.presentation.details.mapper.ProjectsDetailsViewStateMapper
import com.falon.feed.presentation.details.model.ProjectDetailsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val getReadmeUseCase: GetReadmeUseCase,
    private val clearSelectedTrendingProjectsUseCase: ClearSelectedTrendingProjectsUseCase,
    stateFactory: ProjectDetailsStateFactory,
    viewStateMapper: ProjectsDetailsViewStateMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(stateFactory.create())
    val viewState: StateFlow<ProjectDetailsViewState> =
        _state
            .map(viewStateMapper::from)
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(0),
                initialValue = viewStateMapper.from(_state.value)
            )

    init {
        loadReadMe()
    }

    private fun loadReadMe() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                val readme = getReadmeUseCase.execute(
                    owner = it.selectedProject.ownerLogin,
                    repo = it.selectedProject.repositoryName,
                )
                it.copy(
                    readmeContent = readme,
                    loadingReadmeFailed = readme == null
                )
            }
        }
    }

    fun onExitProjectDetails() {
        clearSelectedTrendingProjectsUseCase.execute()
    }

    fun onRefresh() {
        loadReadMe()
    }

    companion object {

        const val STAR_RESOURCE_ARG = "starsResource"
    }
}
