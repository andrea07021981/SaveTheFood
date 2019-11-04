package com.example.savethefood.constants

sealed class LoginAuthenticationStates(open val message: String)

// Initial state, the user needs to authenticate
data class Unauthenticated(override val message: String = "Not logged in") : LoginAuthenticationStates(message)

// Initial state, the user needs to authenticate
data class Authenticated(override val message: String = "Authentication ok") : LoginAuthenticationStates(message)

// Authentication failed
data class InvalidAuthentication(override val message: String = "Authentication error") : LoginAuthenticationStates(message)