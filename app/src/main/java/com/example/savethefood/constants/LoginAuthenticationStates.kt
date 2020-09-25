package com.example.savethefood.constants

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain

sealed class LoginAuthenticationStates(open val message: String)

// Authentication failed
data class Authenticating(
    override val message: String = "Loggin in"
) : LoginAuthenticationStates(message)

// Initial state, the user needs to authenticate
data class Unauthenticated(
    override val message: String = "Not logged in"
) : LoginAuthenticationStates(message)

// Initial state, the user needs to authenticate
data class Authenticated(
    override val message: String = "Authentication ok",
    val user: UserDomain
) : LoginAuthenticationStates(message)

// Authentication failed
data class InvalidAuthentication(
    override val message: String = "Authentication error"
) : LoginAuthenticationStates(message)

data class Idle(
    override val message: String = "None"
) : LoginAuthenticationStates(message)