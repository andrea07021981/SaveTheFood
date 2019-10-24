package com.example.savethefood.login

sealed class LoginAuthenticationStates

// Initial state, the user needs to authenticate
object UNAUTHENTICATED : LoginAuthenticationStates()

// Initial state, the user needs to authenticate
object AUTHENTICATED : LoginAuthenticationStates()

// Authentication failed
object INVALID_AUTHENTICATION : LoginAuthenticationStates()