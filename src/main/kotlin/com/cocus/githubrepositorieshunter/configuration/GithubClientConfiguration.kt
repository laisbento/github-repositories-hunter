package com.cocus.githubrepositorieshunter.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class GithubClientConfiguration {

    @Value("\${client.github-api.base-path}")
    private lateinit var githubClient: String

    @Bean
    fun webClient() = WebClient.create(githubClient)
}