package com.falon.feed.data.di

import com.falon.feed.data.datasource.GitHubApi
import com.falon.feed.data.repository.TrendingProjectRepositoryImpl
import com.falon.feed.domain.repository.TrendingProjectsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class FeedDataModule {

    @Binds
    abstract fun bindTrendingProjectsRepository(
        trendingProjectRepositoryImpl: TrendingProjectRepositoryImpl,
    ): TrendingProjectsRepository

    companion object {

        @Provides
        fun provideGitHubApiService(retrofit: Retrofit): GitHubApi =
            retrofit.create(GitHubApi::class.java)
    }
}
