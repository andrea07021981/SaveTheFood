package com.example.savethefood.shared.data.source.remote.datatransferobject

import com.example.savethefood.shared.data.domain.RecipeDomain
import com.example.savethefood.shared.data.domain.RecipeResult
import kotlinx.serialization.Serializable


@Serializable
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

@Serializable
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
                it.id.toLong(),
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
