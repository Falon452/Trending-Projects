package com.falon.feed.data.datasource

import com.falon.feed.data.model.GitHubSearchResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    data class GitHubQuery(
        val createdAfter: LocalDateTime,
        val minStars: Int
    ) {

        override fun toString(): String {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = createdAfter.format(formatter)
            return "created:>$date+stars:>$minStars"
        }
    }
}
