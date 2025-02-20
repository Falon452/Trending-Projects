package com.falon.feed.presentation.projects.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.domain.usecase.ObserveTrendingProjectsUseCase
import com.falon.feed.domain.usecase.SaveSelectedTrendingProjectsUseCase
import com.falon.feed.presentation.projects.mapper.ProjectsViewStateMapper
import com.falon.feed.presentation.projects.model.ProjectsState
import com.falon.feed.presentation.projects.model.ProjectsViewState
import com.falon.theme.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val observeTrendingProjectsUseCase: ObserveTrendingProjectsUseCase,
    private val saveSelectedTrendingProjectsUseCase: SaveSelectedTrendingProjectsUseCase,
    private val themePreferences: ThemePreferences,
    viewStateMapper: ProjectsViewStateMapper,
) : ViewModel() {

    private val _trendingProjects =
        MutableStateFlow<PagingData<TrendingProject>>(PagingData.empty())
    val trendingProjects: StateFlow<PagingData<TrendingProject>> = _trendingProjects

    private val _state = MutableStateFlow<ProjectsState>(ProjectsState())
    val viewState: StateFlow<ProjectsViewState> =
        _state.map(viewStateMapper::from).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(0),
            initialValue = viewStateMapper.from(_state.value)
        )

    init {
        observeDarkMode()
        observeAfterCreatedDateChanges()
    }

    private fun observeDarkMode() {
        viewModelScope.launch {
            themePreferences.observeIsDarkMode()
                .collectLatest { isDarkMode ->
                    _state.value = _state.value.copy(isDarkMode = isDarkMode)
                }
        }
    }

    private fun observeAfterCreatedDateChanges() {
        viewModelScope.launch {
            _state.map { it.afterCreatedDate }
                .distinctUntilChanged()
                .collectLatest(::loadPagedTrendingProjects)
        }
    }

    private suspend fun loadPagedTrendingProjects(afterCreatedDate: LocalDateTime) {
        _trendingProjects.value = PagingData.empty()
        observeTrendingProjectsUseCase.execute(afterCreatedDate)
            .cachedIn(viewModelScope)
            .collectLatest { pagingData ->
                _trendingProjects.value = pagingData
            }
    }

    fun onTrendingProjectCardClicked(project: TrendingProject) {
        viewModelScope.launch {
            saveSelectedTrendingProjectsUseCase.execute(project)
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            themePreferences.saveDarkMode(!_state.value.isDarkMode)
        }
    }

    fun onDateClicked() {
        _state.value = _state.value.copy(showDatePicker = true)
    }

    fun onDismissRequest() {
        _state.value = _state.value.copy(showDatePicker = false)
    }

    fun onDatePickerConfirmButtonClicked() {
        _state.value = _state.value.copy(showDatePicker = false)
    }

    fun onDateSelected(millis: Long?) {
        millis?.let { millis ->
            val localDateTime = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            _state.value = _state.value.copy(afterCreatedDate = localDateTime)
        }
    }
}
