package com.cocus.githubrepositorieshunter.services

import com.cocus.githubrepositorieshunter.client.GithubClient
import com.cocus.githubrepositorieshunter.models.GithubRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class GithubRepositoriesService(
    private val githubClient: GithubClient
) {

    fun listRepositories(username: String, allowForks: Boolean): Flux<GithubRepository> {
        val repositories = githubClient.getRepositories(username, allowForks)
        return repositories.flatMapMany { repository ->
            Flux.fromIterable(repository.items).flatMap { item ->
                listBranches(item.owner.login, item.name)
                    .collectList()
                    .map { branches ->
                        GithubRepository(item.name, item.owner.login, branches)
                    }
            }
        }
    }

    fun listBranches(username: String, repositoryName: String) = githubClient.getBranches(username, repositoryName)

}