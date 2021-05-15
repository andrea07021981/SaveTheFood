package com.example.savethefood.data.domain

import android.os.Parcelable
import com.example.savethefood.data.source.local.entity.RecipeIngredientEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
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
): Parcelable {
    constructor() : this(0L,0,"","","",0,0,0, 0)

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