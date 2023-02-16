package com.cocus.githubrepositorieshunter.client

import com.cocus.githubrepositorieshunter.exceptions.ServiceUnavailableException
import com.cocus.githubrepositorieshunter.exceptions.UsernameNotFoundException
import com.cocus.githubrepositorieshunter.models.RepositoryItems
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@ExtendWith(MockKExtension::class)
class GithubClientTest {

    @MockK
    lateinit var webClient: WebClient

    @MockK
    lateinit var webClientBuilder: WebClient.RequestHeadersUriSpec<*>

    @MockK
    lateinit var responseSpec: WebClient.ResponseSpec

    @InjectMockKs
    lateinit var githubClient: GithubClient

    private val username = "username"
    private val includeForks = false

    @Test
    @DisplayName("Should throw an exception when the service is unavailable")
    fun `get repositories when service is unavailable then throw exception`() {
        every { webClient.get() } returns webClientBuilder
        every { webClientBuilder.uri("/search/repositories?q=user:$username+fork:$includeForks") } returns webClientBuilder
        every { webClientBuilder.retrieve() } returns responseSpec
        every { responseSpec.onStatus(any(), any()) } returns responseSpec
        every { responseSpec.bodyToMono(RepositoryItems::class.java) } returns Mono.error(ServiceUnavailableException("Service unavailable "))

        assertThrows<ServiceUnavailableException> {
            githubClient.getRepositories(username, includeForks).block()
        }
    }

    @Test
    @DisplayName("Should return a list of repositories when the username is found")
    fun `get repositories when username is found then return list of repositories`() {
        val repositoryItems = RepositoryItems(listOf())

        every { webClient.get() } returns webClientBuilder
        every { webClientBuilder.uri("/search/repositories?q=user:$username+fork:$includeForks") } returns webClientBuilder
        every { webClientBuilder.retrieve() } returns responseSpec
        every { responseSpec.onStatus(any(), any()) } returns responseSpec
        every { responseSpec.bodyToMono(RepositoryItems::class.java) } returns Mono.just(repositoryItems)

        val result = githubClient.getRepositories(username, includeForks)

        assertEquals(repositoryItems, result.block())
    }

    @Test
    @DisplayName("Should throw an exception when the username is not found")
    fun `get repositories when username is not found then throw exception`() {
        every { webClient.get() } returns webClientBuilder
        every { webClientBuilder.uri("/search/repositories?q=user:$username+fork:$includeForks") } returns webClientBuilder
        every { webClientBuilder.retrieve() } returns responseSpec
        every { responseSpec.onStatus(any(), any()) } returns responseSpec
        every { responseSpec.bodyToMono(RepositoryItems::class.java) } returns Mono.error(UsernameNotFoundException(username, "Username not found"))

        assertThrows<UsernameNotFoundException> {
            githubClient.getRepositories(username, includeForks).block()
        }
    }

}