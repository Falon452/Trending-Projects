package com.falon.feed.presentation.projects.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.domain.usecase.ObserveTrendingProjectsUseCase
import com.falon.feed.domain.usecase.SaveSelectedTrendingProjectsUseCase
import com.falon.feed.presentation.di.DefaultDispatcher
import com.falon.feed.presentation.di.IoDispatcher
import com.falon.feed.presentation.projects.mapper.ProjectsViewStateMapper
import com.falon.feed.presentation.projects.model.ProjectsState
import com.falon.feed.presentation.projects.model.ProjectsViewState
import com.falon.theme.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val observeTrendingProjectsUseCase: ObserveTrendingProjectsUseCase,
    private val saveSelectedTrendingProjectsUseCase: SaveSelectedTrendingProjectsUseCase,
    private val themePreferences: ThemePreferences,
    viewStateMapper: ProjectsViewStateMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _trendingProjects =
        MutableStateFlow<PagingData<TrendingProject>>(PagingData.empty())
    val trendingProjects: StateFlow<PagingData<TrendingProject>> = _trendingProjects

    private val _state = MutableStateFlow(ProjectsState())
    @VisibleForTesting
    val state: ProjectsState get() = _state.value
    val viewState: StateFlow<ProjectsViewState> =
        _state.map(viewStateMapper::from)
            .flowOn(defaultDispatcher)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(0),
                initialValue = viewStateMapper.from(_state.value)
            )

    init {
        observeDarkMode()
        loadPagedTrendingProjects()
    }

    private fun observeDarkMode() {
        viewModelScope.launch(ioDispatcher) {
            themePreferences.observeIsDarkMode()
                .collectLatest { isDarkMode ->
                    _state.update { it.copy(isDarkMode = isDarkMode) }
                }
        }
    }

    private fun loadPagedTrendingProjects() {
        viewModelScope.launch(ioDispatcher) {
            _trendingProjects.update { PagingData.empty() }
            observeTrendingProjectsUseCase.execute(afterCreatedDate = _state.value.afterCreatedDate)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _trendingProjects.update { pagingData }
                }
        }
    }

    fun onTrendingProjectCardClicked(project: TrendingProject) {
        viewModelScope.launch(ioDispatcher) {
            saveSelectedTrendingProjectsUseCase.execute(project)
        }
    }

    fun onToggleTheme() {
        viewModelScope.launch(ioDispatcher) {
            themePreferences.saveDarkMode(!_state.value.isDarkMode)
        }
    }

    fun onDateClicked() {
        _state.update { it.copy(showDatePicker = true) }
    }

    fun onDismissRequest() {
        _state.update { it.copy(showDatePicker = false) }
    }

    fun onDatePickerConfirmButtonClicked() {
        _state.update { it.copy(showDatePicker = false) }
    }

    fun onDateSelected(millis: Long?) {
        millis?.let { millis ->
            val localDateTime = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()

            if (localDateTime.toLocalDate() != _state.value.afterCreatedDate.toLocalDate()) {
                _state.update {
                    it.copy(afterCreatedDate = localDateTime)
                }

                loadPagedTrendingProjects()
            }
        }
    }
}
