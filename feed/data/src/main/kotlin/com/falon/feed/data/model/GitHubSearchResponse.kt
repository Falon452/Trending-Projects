package com.falon.feed.data.model

import com.google.gson.annotations.SerializedName

internal data class GitHubSearchResponse(
    @SerializedName("items") val repositories: List<Repository>?,
)

internal data class Repository(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val repositoryName: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("html_url") val htmlUrl: String?,
    @SerializedName("stargazers_count") val stars: Int?,
    @SerializedName("owner") val owner: Owner?,
)

internal data class Owner(
    @SerializedName("login") val login: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
)
