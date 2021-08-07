package com.example.savethefood.shared.data.domain

import com.example.savethefood.shared.Parcelable
import com.example.savethefood.shared.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class RecipeDomain(
    val baseUri: String = "",
    val expires: Long = 0L,
    val isStale: Boolean = false,
    val number: Int = 0,
    val offset: Int = 0,
    val processingTimeMs: Int = 0,
    val results: List<RecipeResult>?,
    val totalResults: Int
) : Parcelable