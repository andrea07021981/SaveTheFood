package com.example.savethefood.constants

// TODO remove it and create an abstract class in sign up with password strength
enum class LoginError(
    val message: String
) {
    INVALID_USERNAME("Username not valid"),
    INVALID_EMAIL("Email not valid"),
    INVALID_PASSWORD("Password not valid"),
    NONE("")
}