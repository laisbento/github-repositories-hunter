package com.cocus.githubrepositorieshunter.services

import com.cocus.githubrepositorieshunter.client.GithubClient
import com.cocus.githubrepositorieshunter.models.*
import com.cocus.githubrepositorieshunter.utils.MockBuilder.branchesMock
import com.cocus.githubrepositorieshunter.utils.MockBuilder.repositoryItemsMock
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockKExtension::class)
class GithubRepositoriesServiceTest {

    @MockK
    private lateinit var githubClient: GithubClient

    @InjectMockKs
    private lateinit var githubRepositoriesService: GithubRepositoriesService

    @Test
    @DisplayName("Should return a list of repositories with branches")
    fun `list repositories should return a list of repositories with branches`() {
        val repositoryItems = repositoryItemsMock()
        val branches = branchesMock()
        val expected = listOf(
            GithubRepository("repo1", "owner1", branches),
            GithubRepository("repo2", "owner2", branches)
        )

        every { githubClient.getRepositories("user", false) } returns Mono.just(repositoryItems)
        every { githubClient.getBranches("owner1", "repo1") } returns Flux.fromIterable(branches)
        every { githubClient.getBranches("owner2", "repo2") } returns Flux.fromIterable(branches)

        val actual = githubRepositoriesService.listRepositories("user", false)

        StepVerifier.create(actual)
            .expectNextSequence(expected)
            .verifyComplete()
    }

    @Test
    @DisplayName("Should return a list of repositories")
    fun `list repositories should return a list of repositories`() {
        val repositoryItems = repositoryItemsMock()
        val branches = branchesMock()
        every { githubClient.getRepositories("user", false) } returns Mono.just(repositoryItems)
        every { githubClient.getBranches("owner1", "repo1") } returns Flux.fromIterable(branches)
        every { githubClient.getBranches("owner2", "repo2") } returns Flux.fromIterable(branches)

        val result = githubRepositoriesService.listRepositories("user", false)

        StepVerifier.create(result)
            .expectNextMatches {
                it.repositoryName == "repo1" && it.ownerName == "owner1" && it.branches == branches
            }
            .expectNextMatches {
                it.repositoryName == "repo2" && it.ownerName == "owner2" && it.branches == branches
            }
            .verifyComplete()
    }

}