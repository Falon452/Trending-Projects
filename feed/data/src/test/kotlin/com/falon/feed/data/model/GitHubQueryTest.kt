package com.falon.feed.data.model

import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class GitHubQueryTest {

    @Test
    fun `test toString method formats correctly`() {
        val createdAfter = LocalDateTime.of(2023, 3, 15, 10, 30, 0, 0)
        val minStars = 50
        val gitHubQuery = GitHubQuery(createdAfter, minStars)

        val result = gitHubQuery.toString()

        val expected = "created:>2023-03-15+stars:>50"
        assertEquals(expected, result)
    }

    @Test
    fun `test toString with different date and stars`() {
        val createdAfter = LocalDateTime.of(2022, 12, 1, 14, 0, 0, 0)
        val minStars = 100
        val gitHubQuery = GitHubQuery(createdAfter, minStars)

        val result = gitHubQuery.toString()

        val expected = "created:>2022-12-01+stars:>100"
        assertEquals(expected, result)
    }
}
