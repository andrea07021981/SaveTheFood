package com.example.savethefood.shared.utils

@Suppress("NO_ACTUAL_FOR_EXPECT") // TODO REMOVE IT, BUG
expect class Logger {

    companion object Default{
        fun log(tag: String = "", message: String)
    }
}