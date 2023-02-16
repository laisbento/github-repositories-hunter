package com.cocus.githubrepositorieshunter.exceptions

import com.cocus.githubrepositorieshunter.models.ResponseError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GithubRepositoryControllerAdvice {

    @ExceptionHandler(AcceptHeaderException::class)
    fun handleUnsupportedMediaTypeException(
        ex: AcceptHeaderException
    ): ResponseEntity<ResponseError> {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ResponseError(HttpStatus.NOT_ACCEPTABLE.value(), ex.message!!))
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleGithubUsernameNotFoundException(
        ex: UsernameNotFoundException
    ): ResponseEntity<ResponseError> {
        val error = "${ex.username} was not found."
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseError(HttpStatus.NOT_FOUND.value(), error))
    }

    @ExceptionHandler(BranchNotFoundException::class)
    fun handleBranchNotFoundException(
        ex: BranchNotFoundException
    ) = ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseError(HttpStatus.NOT_FOUND.value(), "${ex.message}"))

    @ExceptionHandler(ServiceUnavailableException::class)
    fun handleServiceUnavailableException(
        ex: ServiceUnavailableException
    ): ResponseEntity<ResponseError> {
        val error = "${ex.message}. Try again later"
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ResponseError(HttpStatus.SERVICE_UNAVAILABLE.value(), error))
    }
}