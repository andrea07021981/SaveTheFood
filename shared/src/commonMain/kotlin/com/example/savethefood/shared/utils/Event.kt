package com.example.savethefood.shared.utils

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
@Suppress("NO_ACTUAL_FOR_EXPECT") // TODO REMOVE IT, BUG
expect open class Event<out T>(content: T) {

}
