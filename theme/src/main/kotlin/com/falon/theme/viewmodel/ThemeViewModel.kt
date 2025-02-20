package com.falon.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falon.theme.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    themePreferences: ThemePreferences,
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean> = themePreferences.observeIsDarkMode()
        .stateIn(viewModelScope, SharingStarted.Lazily, false)
}
