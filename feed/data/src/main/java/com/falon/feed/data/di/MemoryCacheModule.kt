package com.falon.feed.data.di

import com.falon.feed.data.datasource.MemoryCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MemoryCacheModule {

    @Provides
    @Singleton
    fun provideMemoryCache(): MemoryCache = MemoryCache
}
