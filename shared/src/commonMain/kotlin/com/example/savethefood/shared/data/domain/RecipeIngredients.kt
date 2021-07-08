package com.example.savethefood.data.domain

import kotlinx.serialization.Serializable
import com.example.savethefood.data.source.local.entity.RecipeIngredientEntity

@Serializable
data class RecipeIngredients(
    var recipeId: Long = 0,
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,
    val likes: Int,
    val missedIngredientCount: Int,
    val usedIngredientCount: Int,
    val unUsedIngredientCount: Int,
) {
    val totalIngredients: Int
        get() = usedIngredientCount + unUsedIngredientCount
}

fun RecipeIngredients.asDatabaseModel(): RecipeIngredientEntity {
    return RecipeIngredientEntity(
        recipeId = recipeId,
        id = id,
        title = title,
        image = image,
        imageType = imageType,
        likes = likes,
        missedIngredientCount = missedIngredientCount,
        usedIngredientCount = usedIngredientCount,
        unUsedIngredientCount = unUsedIngredientCount
    )
}