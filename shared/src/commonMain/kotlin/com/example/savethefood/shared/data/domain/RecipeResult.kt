package com.example.savethefood.shared.data.domain

import kotlinx.serialization.Serializable

@Serializable
data class RecipeResult(
    val recipeId: Long = 0, // Id to distinguish the local and remote recipe
    val id: Long,
    val baseDomainUrl: String = "",
    val image: String = "",
    val sourceUrl: String?,
    val readyInMinutes: Int = 0,
    val servings: Int = 0,
    val title: String = ""
)