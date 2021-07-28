package com.example.savethefood.shared.utils

actual open class Event<out T> actual constructor(private val content: T) {
}