package com.falon.feed.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.domain.usecase.ObserveTrendingProjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val observeTrendingProjectsUseCase: ObserveTrendingProjectsUseCase,
) : ViewModel() {

    private val _trendingProjects =
        MutableStateFlow<PagingData<TrendingProject>>(PagingData.empty())
    val trendingProjects: StateFlow<PagingData<TrendingProject>> = _trendingProjects

    init {
        viewModelScope.launch {
            observeTrendingProjectsUseCase.execute()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _trendingProjects.value = pagingData
                }
        }
    }
}
