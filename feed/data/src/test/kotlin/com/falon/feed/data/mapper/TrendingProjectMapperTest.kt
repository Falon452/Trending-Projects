package com.falon.feed.data.mapper

import com.falon.feed.data.model.Owner
import com.falon.feed.data.model.Repository
import com.falon.feed.domain.model.TrendingProject
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class TrendingProjectMapperTest {

    private val trendingProjectMapper = TrendingProjectMapper()

    private val validRepository = Repository(
        id = 1L,
        repositoryName = "Test Repo",
        description = "Test description",
        htmlUrl = "http://repo.url",
        stars = 100,
        owner = Owner(
            login = "owner",
            avatarUrl = "http://avatar.url"
        )
    )

    private val repositoryWithMissingId = validRepository.copy(id = null)
    private val repositoryWithMissingOwner = validRepository.copy(owner = null)
    private val repositoryWithMissingRepositoryName = validRepository.copy(repositoryName = null)
    private val repositoryWithMissingHtmlUrl = validRepository.copy(htmlUrl = null)
    private val repositoryWithMissingStars = validRepository.copy(stars = null)
    private val repositoryWithMissingOwnerAvatarUrl =
        validRepository.copy(owner = validRepository.owner?.copy(avatarUrl = null))

    @Test
    fun `WHEN from is called with a valid repository, THEN it returns a mapped TrendingProject`() {
        val result = trendingProjectMapper.from(validRepository)

        val expected = TrendingProject(
            id = "1",
            ownerLogin = "owner",
            ownerAvatarUrl = "http://avatar.url",
            repositoryName = "Test Repo",
            htmlUrl = "http://repo.url",
            stars = 100,
            description = "Test description"
        )

        assertEquals(expected, result)
    }

    @Test
    fun `WHEN from is called with a repository with missing id, THEN it returns null`() {
        val result = trendingProjectMapper.from(repositoryWithMissingId)

        assertNull(result)
    }

    @Test
    fun `WHEN from is called with a repository with missing owner, THEN it returns null`() {
        val result = trendingProjectMapper.from(repositoryWithMissingOwner)

        assertNull(result)
    }

    @Test
    fun `WHEN from is called with a repository with missing repository name, THEN it returns null`() {
        val result = trendingProjectMapper.from(repositoryWithMissingRepositoryName)

        assertNull(result)
    }

    @Test
    fun `WHEN from is called with a repository with missing htmlUrl, THEN it returns null`() {
        val result = trendingProjectMapper.from(repositoryWithMissingHtmlUrl)

        assertNull(result)
    }

    @Test
    fun `WHEN from is called with a repository with missing stars, THEN it returns null`() {
        val result = trendingProjectMapper.from(repositoryWithMissingStars)

        assertNull(result)
    }

    @Test
    fun `WHEN from is called with a repository with missing owner avatar URL, THEN it returns null`() {
        val result = trendingProjectMapper.from(repositoryWithMissingOwnerAvatarUrl)

        assertNull(result)
    }
}
