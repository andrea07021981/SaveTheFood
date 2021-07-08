package com.example.savethefood.data.domain

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDomain(
    val baseUri: String = "",
    val expires: Long = 0L,
    val isStale: Boolean = false,
    val number: Int = 0,
    val offset: Int = 0,
    val processingTimeMs: Int = 0,
    val results: List<RecipeResult>?,
    val totalResults: Int
)