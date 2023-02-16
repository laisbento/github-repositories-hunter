package com.cocus.githubrepositorieshunter.controllers

import com.cocus.githubrepositorieshunter.exceptions.AcceptHeaderException
import com.cocus.githubrepositorieshunter.exceptions.UsernameNotFoundException
import com.cocus.githubrepositorieshunter.services.GithubRepositoriesService
import com.cocus.githubrepositorieshunter.utils.MockBuilder.githubRepositoryMock
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

@ExtendWith(MockKExtension::class)
class GithubRepositoriesControllerTest {

    @MockK
    private lateinit var githubRepositoriesService: GithubRepositoriesService

    @InjectMockKs
    private lateinit var githubRepositoriesController: GithubRepositoriesController

    private val username = "username"
    private val includeForks = false
    private val contentType = MediaType.APPLICATION_JSON_VALUE
    private val githubRepository = githubRepositoryMock()
    private val expectedResponse = ResponseEntity.status(HttpStatus.OK).body(githubRepository)
    private val responseBody = expectedResponse.body?.toStream()?.findFirst()?.get()

    @Test
    @DisplayName("Should throw an exception when the content type is not supported")
    fun `get repositories when content type is not supported then throw exception`() {
        val includeForks = false
        val contentType = "application/xml"
        val expectedExceptionMessage = "application/xml"

        val exception = assertThrows<AcceptHeaderException> {
            githubRepositoriesController.getRepositories(username, includeForks, contentType)
        }

        assertEquals(expectedExceptionMessage, exception.mediaType)
    }

    @Test
    @DisplayName("Should return a list of repositories when the username is valid")
    fun `get repositories when username is valid`() {
        every { githubRepositoriesService.listRepositories(username, includeForks) } returns githubRepository

        val actualResponse = githubRepositoriesController.getRepositories(username, includeForks, contentType)
        val actualResponseBody = actualResponse.body?.toStream()?.findFirst()?.get()

        assertEquals(expectedResponse.statusCode, actualResponse.statusCode)
        assertEquals(responseBody?.repositoryName, actualResponseBody?.repositoryName)
        assertEquals(responseBody?.ownerName, actualResponseBody?.ownerName)
    }

    @Test
    @DisplayName("Should return a list of repositories when the username is valid and include forks is true")
    fun `get repositories when username is valid and include forks is true`() {
        every { githubRepositoriesService.listRepositories(username, includeForks) } returns githubRepository

        val actualResponse = githubRepositoriesController.getRepositories(username, includeForks, contentType)
        val actualResponseBody = actualResponse.body?.toStream()?.findFirst()?.get()

        assertEquals(expectedResponse.statusCode, actualResponse.statusCode)
        assertEquals(responseBody?.repositoryName, actualResponseBody?.repositoryName)
        assertEquals(responseBody?.ownerName, actualResponseBody?.ownerName)
    }

    @Test
    @DisplayName("Should return an error when the username is invalid")
    fun `return error when username is invalid`() {
        every { githubRepositoriesService.listRepositories(username, includeForks) } throws UsernameNotFoundException(username, "Error")

        val exception = assertThrows<UsernameNotFoundException> {
            githubRepositoriesController.getRepositories(username, includeForks, contentType)
        }

        assertEquals("Error", exception.message)
    }

}