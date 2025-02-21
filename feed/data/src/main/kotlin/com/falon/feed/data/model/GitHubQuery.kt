package com.falon.feed.data.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
