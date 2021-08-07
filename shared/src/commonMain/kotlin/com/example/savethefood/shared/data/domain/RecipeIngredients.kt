package com.example.savethefood.shared.data.domain

import com.example.savethefood.shared.Parcelable
import com.example.savethefood.shared.Parcelize
import kotlinx.serialization.Serializable
import com.example.savethefood.shared.data.source.local.entity.RecipeIngredientEntity

@Serializable
@Parcelize
data class RecipeIngredients(
    var recipeId: Long = 0,
    val id: Long,
    val title: String,
    val image: String,
    val imageType: String,
    val likes: Int,
    val missedIngredientCount: Int,
    val usedIngredientCount: Int,
    val unUsedIngredientCount: Int,
) : Parcelable{
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