package com.cocus.githubrepositorieshunter.controllers

import com.cocus.githubrepositorieshunter.exceptions.AcceptHeaderException
import com.cocus.githubrepositorieshunter.models.GithubRepository
import com.cocus.githubrepositorieshunter.services.GithubRepositoriesService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/github")
class GithubRepositoriesController(
    private val githubRepositoriesService: GithubRepositoriesService
) {

    @ApiOperation("Get github repositories based on a valid username")
    @GetMapping
    fun getRepositories(
        @RequestParam username: String,
        @RequestParam allowForks: Boolean = false,
        @RequestHeader("Accept") accept: String
    ): ResponseEntity<Flux<GithubRepository>> {
        if (MediaType.APPLICATION_JSON_VALUE == accept) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(githubRepositoriesService.listRepositories(username, allowForks))
        }
        throw AcceptHeaderException("Invalid accept header")
    }
}
