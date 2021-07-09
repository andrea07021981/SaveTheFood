package com.example.savethefood.shared.data.source.remote.service

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

actual class KtorClientFactory {
    actual fun build(): HttpClient {
        return HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true // Ignore extra fiends sent by the server
                    }
                )
            }
            install(Logging) // Log all the Ktor requests
        }
    }
}