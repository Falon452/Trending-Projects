package com.falon.theme.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.falon.theme.ThemePreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "themeDataStore")

@Singleton
internal class ThemePreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) : ThemePreferences {

    private val dataStore = context.dataStore

    override fun observeIsDarkMode(): Flow<Boolean> =
        dataStore.data
            .map { preferences ->
                preferences[DARK_MODE_KEY] == true
            }

    override suspend fun saveDarkMode(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = isDark
        }
    }

    companion object {

        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }
}
