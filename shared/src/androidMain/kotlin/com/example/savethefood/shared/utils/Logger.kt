package com.example.savethefood.shared.utils

import android.util.Log

actual class Logger {
    actual companion object Default {
        actual fun log(tag: String, message: String) {
            Log.d(tag, message)
        }
    }
}