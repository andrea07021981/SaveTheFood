package com.example.savethefood.constants

enum class LoginError(
    val message: String
) {
    INVALID_EMAIL("Email not valid"),
    INVALID_PASSWORD("Password not valid"),
    NONE("")
}