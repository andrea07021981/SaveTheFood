package com.example.savethefood.data.source.remote.datatransferobject

import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeResult
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkRecipe(
    val baseUri: String,
    val expires: Long,
    val isStale: Boolean,
    val number: Int,
    val offset: Int,
    val processingTimeMs: Int,
    val results: List<Result>,
    val totalResults: Int
)

@JsonClass(generateAdapter = true)
data class Result(
    val id: Int,
    val image: String,
    val sourceUrl: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val title: String
)

fun NetworkRecipe.asDomainModel() : RecipeDomain {
    return RecipeDomain(
        baseUri = baseUri,
        expires = expires,
        isStale = isStale,
        number = number,
        offset = offset,
        processingTimeMs = processingTimeMs,
        results = results.map {
            RecipeResult(
                0L,
                it.id,
                baseUri,
                it.image,
                it.sourceUrl,
                it.readyInMinutes,
                it.servings,
                it.title
            )
        },
        totalResults = totalResults
    )
}
