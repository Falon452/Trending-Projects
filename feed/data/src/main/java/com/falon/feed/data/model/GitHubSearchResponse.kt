package com.falon.feed.data.model

import com.google.gson.annotations.SerializedName

internal data class GitHubSearchResponse(
    @SerializedName("items") val repositories: List<Repository>?,
)

internal data class Repository(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("html_url") val url: String?,
    @SerializedName("stargazers_count") val stars: Int?,
)
