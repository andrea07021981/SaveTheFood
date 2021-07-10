package com.example.savethefood.shared.utils

enum class LoginStateValue(
    val message: String
) {
    INVALID_FORMAT("Format incorrect"),
    INVALID_LENGTH("Length incorrect"),
    EMPTY_FIELD("No value"),
    NONE("")
}