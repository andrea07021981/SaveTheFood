package com.example.savethefood.shared.utils

actual class Logger {

    actual companion object Default{
        actual fun log(tag: String, message: String) {
            println(message)
        }
    }
}