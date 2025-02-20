package com.falon.theme.di

import com.falon.theme.ThemePreferences
import com.falon.theme.data.ThemePreferencesDataStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ThemeModule {

    @Binds
    @Singleton
    abstract fun bindThemePreferences(
        themePreferencesDataStore: ThemePreferencesDataStore
    ): ThemePreferences
}
