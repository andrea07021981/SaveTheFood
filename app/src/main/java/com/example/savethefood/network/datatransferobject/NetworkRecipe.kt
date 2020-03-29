package com.example.savethefood.network.datatransferobject

import com.example.savethefood.local.domain.RecipeDomain
import com.example.savethefood.local.domain.RecipeResult
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
    val imageUrls: List<String>,
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
                it.id,
                baseUri,
                it.image,
                it.imageUrls.first(),
                it.readyInMinutes,
                it.servings,
                it.title) },
        totalResults = totalResults
    )
}
