package com.falon.feed.data.model

import com.google.gson.annotations.SerializedName

internal data class ReadmeResponse(
    @SerializedName("content") val content: String?,
    @SerializedName("encoding") val encoding: String?,
)
