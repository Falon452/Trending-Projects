package com.falon.feed.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falon.feed.domain.usecase.ObserveTrendingProjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val observeTrendingProjectsUseCase: ObserveTrendingProjectsUseCase,
) : ViewModel() {

    init {
        viewModelScope.launch {
            observeTrendingProjectsUseCase.execute().collect {

            }
        }
    }
}
