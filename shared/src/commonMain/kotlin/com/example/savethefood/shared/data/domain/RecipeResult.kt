package com.example.savethefood.data.domain

import kotlinx.serialization.Serializable

@Serializable
data class RecipeResult(
    val recipeId: Long = 0, // Id to distinguish the local and remote recipe
    val id: Int,
    val baseDomainUrl: String = "",
    val image: String = "",
    val sourceUrl: String?,
    val readyInMinutes: Int = 0,
    val servings: Int = 0,
    val title: String = ""
)