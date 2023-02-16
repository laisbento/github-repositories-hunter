package com.cocus.githubrepositorieshunter.models

data class Repository(
    val name: String,
    val owner: Owner,
    val branch: Branch?
)

