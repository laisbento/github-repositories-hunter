package com.cocus.githubrepositorieshunter.utils

import com.cocus.githubrepositorieshunter.models.*
import reactor.core.publisher.Flux

object MockBuilder {
    fun githubRepositoryMock() = Flux.just(
        GithubRepository(
            "repositoryName", "ownerName", listOf(Branch("branchName", Commit("commitId")))
        )
    )

    fun branchesMock() = listOf(
        Branch("branch1", Commit("commit1")),
        Branch("branch2", Commit("commit2"))
    )

    fun repositoryItemsMock() = RepositoryItems(
        listOf(
            Repository("repo1", Owner("owner1"), null),
            Repository("repo2", Owner("owner2"), null)
        )
    )
}