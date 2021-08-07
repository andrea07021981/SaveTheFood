package com.example.savethefood.shared.data.domain

import com.example.savethefood.shared.Parcelable
import com.example.savethefood.shared.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class RecipeResult(
    val recipeId: Long = 0, // Id to distinguish the local and remote recipe
    val id: Long = 0,
    val baseDomainUrl: String = "",
    val image: String = "",
    val sourceUrl: String? = null,
    val readyInMinutes: Int = 0,
    val servings: Int = 0,
    val title: String = ""
) : Parcelable