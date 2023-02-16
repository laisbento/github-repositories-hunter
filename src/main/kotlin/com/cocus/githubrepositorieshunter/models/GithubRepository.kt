package com.cocus.githubrepositorieshunter.models

data class GithubRepository(
    val repositoryName: String,
    val ownerName: String,
    val branches: List<Branch>
)
