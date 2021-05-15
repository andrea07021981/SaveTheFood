package com.example.savethefood.data.domain

import android.os.Parcelable
import com.example.savethefood.data.source.local.entity.RecipeEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipeIngredients(
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,
    val likes: Int,
    val missedIngredientCount: Int,
    val usedIngredientCount: Int,
    val unUsedIngredientCount: Int,
    var saved: Boolean
): Parcelable {
    constructor() : this(0,"","","",0,0,0, 0, false)

    val totalIngredients: Int
        get() = usedIngredientCount + unUsedIngredientCount
}

fun RecipeIngredients.asDatabaseModel(): RecipeEntity {
    return RecipeEntity(
        id = id,
        title = title,
        image = image,
        imageType = imageType,
        likes = likes,
        missedIngredientCount = missedIngredientCount,
        usedIngredientCount = usedIngredientCount,
        unUsedIngredientCount = unUsedIngredientCount,
        saved = saved
    )
}