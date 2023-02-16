package com.cocus.githubrepositorieshunter.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    companion object {
        private const val API_NAME = "Github Repositories Hunter API"
        private const val BASE_PACKAGE = "com.cocus.githubrepositorieshunter.controllers"
    }

    @Bean
    fun api(): Docket? {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(
                ApiInfoBuilder()
                .title(API_NAME)
                .build())
            .select()
            .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
            .build()
            .useDefaultResponseMessages(false)
            .enableUrlTemplating(false)
    }
}