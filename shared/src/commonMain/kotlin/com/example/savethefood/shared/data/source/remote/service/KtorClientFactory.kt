package com.example.savethefood.shared.data.source.remote.service

import io.ktor.client.*

@Suppress("NO_ACTUAL_FOR_EXPECT") // TODO REMOVE IT, BUG
expect class KtorClientFactory() {
    fun build(): HttpClient
}
