package com.falon.feed.data.datasource

import com.falon.feed.data.model.GitHubQuery
import com.falon.feed.data.model.GitHubSearchResponse
import com.falon.feed.data.model.ReadmeResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GitHubApi {

    @Headers("Accept: application/vnd.github+json")
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q", encoded = true) query: GitHubQuery,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1
    ): GitHubSearchResponse

    @GET("repos/{owner}/{repo}/readme")
    suspend fun getReadme(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): ReadmeResponse
}
