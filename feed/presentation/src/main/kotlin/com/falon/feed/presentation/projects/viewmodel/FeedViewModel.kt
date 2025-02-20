package com.falon.feed.presentation.projects.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.domain.usecase.ObserveTrendingProjectsUseCase
import com.falon.feed.domain.usecase.SaveSelectedTrendingProjectsUseCase
import com.falon.theme.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val observeTrendingProjectsUseCase: ObserveTrendingProjectsUseCase,
    private val saveSelectedTrendingProjectsUseCase: SaveSelectedTrendingProjectsUseCase,
    private val themePreferences: ThemePreferences,
) : ViewModel() {

    private val _trendingProjects =
        MutableStateFlow<PagingData<TrendingProject>>(PagingData.empty())
    val trendingProjects: StateFlow<PagingData<TrendingProject>> = _trendingProjects

    private val _darkMode = MutableStateFlow<Boolean>(false)
    val darkMode: StateFlow<Boolean> = _darkMode

    init {
        loadPagedTrendingProjects()
        observeDarkMode()
    }

    private fun loadPagedTrendingProjects() {
        viewModelScope.launch {
            observeTrendingProjectsUseCase.execute()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _trendingProjects.value = pagingData
                }
        }
    }

    private fun observeDarkMode() {
        viewModelScope.launch {
            themePreferences.observeIsDarkMode()
                .collectLatest { isDarkMode ->
                    _darkMode.value = isDarkMode
                }
        }
    }

    fun onTrendingProjectCardClicked(project: TrendingProject) {
        viewModelScope.launch {
            saveSelectedTrendingProjectsUseCase.execute(project)
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            themePreferences.saveDarkMode(!_darkMode.value)
        }
    }
}
