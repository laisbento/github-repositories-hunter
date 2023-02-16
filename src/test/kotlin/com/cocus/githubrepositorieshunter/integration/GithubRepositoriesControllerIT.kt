package com.cocus.githubrepositorieshunter.integration

import com.cocus.githubrepositorieshunter.client.GithubClient
import com.cocus.githubrepositorieshunter.models.GithubRepository
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockKExtension::class)
class GithubRepositoriesControllerIT {

    @MockK
    private lateinit var githubClient: GithubClient

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `given a valid and existing username, should return all their repositories`() {
        val username = "laisbento"
        webTestClient.get()
            .uri("/github?username=$username")
            .header("Accept", MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(GithubRepository::class.java)
    }

    @Test
    fun `given an invalid username, should return not found`() {
        val username = "laisbentodsdas"
        webTestClient.get()
            .uri("/github?username=$username")
            .header("Accept", MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isNotFound
    }

}

