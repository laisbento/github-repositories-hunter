package com.cocus.githubrepositorieshunter.exceptions

class UsernameNotFoundException(val username: String, message: String) : RuntimeException(message)