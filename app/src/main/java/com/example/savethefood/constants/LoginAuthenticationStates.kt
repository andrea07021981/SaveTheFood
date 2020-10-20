package com.example.savethefood.constants

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.UserDomain

sealed class LoginAuthenticationStates {

    // Authentication failed
    data class Authenticating(
        val message: String = "Loggin in"
    ) : LoginAuthenticationStates()

    // Initial state, the user needs to authenticate
    data class Unauthenticated(
        val message: String = "Not logged in"
    ) : LoginAuthenticationStates()

    // Initial state, the user needs to authenticate
    data class Authenticated(
        val message: String = "Authentication ok",
        val user: UserDomain
    ) : LoginAuthenticationStates()

    // Authentication failed
    data class InvalidAuthentication(
        val message: String = "Authentication error"
    ) : LoginAuthenticationStates()

    object Idle : LoginAuthenticationStates()
}
