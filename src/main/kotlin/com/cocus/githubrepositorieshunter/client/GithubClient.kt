package com.cocus.githubrepositorieshunter.client

import com.cocus.githubrepositorieshunter.exceptions.BranchNotFoundException
import com.cocus.githubrepositorieshunter.exceptions.ServiceUnavailableException
import com.cocus.githubrepositorieshunter.exceptions.UsernameNotFoundException
import com.cocus.githubrepositorieshunter.models.Branch
import com.cocus.githubrepositorieshunter.models.RepositoryItems
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class GithubClient(
    val webClient: WebClient
) {
    fun getRepositories(username: String, allowForks: Boolean) = webClient.get()
        .uri("/search/repositories?q=user:$username+fork:$allowForks")
        .retrieve()
        .onStatus(
            HttpStatus.UNPROCESSABLE_ENTITY::equals
        ) { Mono.just(UsernameNotFoundException(username, "Username not found")) }
        .onStatus(
            HttpStatus.SERVICE_UNAVAILABLE::equals
        ) { Mono.just(ServiceUnavailableException("Service unavailable")) }
        .bodyToMono(RepositoryItems::class.java)

    fun getBranches(username: String, repositoryName: String) = webClient.get()
        .uri("/repos/$username/$repositoryName/branches")
        .retrieve()
        .onStatus(
            HttpStatus.NOT_FOUND::equals
        ) { Mono.just(BranchNotFoundException("Unable to retrieve the branches for the repository $repositoryName")) }
        .onStatus(
            HttpStatus.SERVICE_UNAVAILABLE::equals
        ) { Mono.just(ServiceUnavailableException("Service unavailable")) }
        .bodyToFlux(Branch::class.java)

}