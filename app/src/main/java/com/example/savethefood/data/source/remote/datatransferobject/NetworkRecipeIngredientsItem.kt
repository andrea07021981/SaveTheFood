package com.example.savethefood.data.source.remote.datatransferobject

import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.data.domain.RecipeResult
import com.squareup.moshi.JsonClass

// Use this structure only if the the json starts with "items [...]"
//@JsonClass(generateAdapter = true)
//data class NetworkRecipeIngredients(val items: List<NetworkRecipeIngredientsItem>)


@JsonClass(generateAdapter = true)
data class NetworkRecipeIngredientsItem(
    val id: Int,
    val image: String,
    val imageType: String,
    val likes: Int,
    val missedIngredientCount: Int,
    val missedIngredients: List<MissedIngredient>,
    val title: String,
    val unusedIngredients: List<UnusedIngredient>,
    val usedIngredientCount: Int,
    val usedIngredients: List<UsedIngredient>
)

@JsonClass(generateAdapter = true)
data class UnusedIngredient(
    val aisle: String,
    val amount: Double,
    val id: Int,
    val image: String,
    val meta: List<Any>,
    val metaInformation: List<Any>,
    val name: String,
    val original: String,
    val originalName: String,
    val originalString: String,
    val unit: String,
    val unitLong: String,
    val unitShort: String
)

@JsonClass(generateAdapter = true)
data class UsedIngredient(
    val aisle: String,
    val amount: Double,
    val extendedName: String?,
    val id: Int,
    val image: String,
    val meta: List<String>,
    val metaInformation: List<String>,
    val name: String,
    val original: String,
    val originalName: String,
    val originalString: String,
    val unit: String,
    val unitLong: String,
    val unitShort: String
)

@JsonClass(generateAdapter = true)
data class MissedIngredient(
    val aisle: String?,
    val amount: Double,
    val id: Int,
    val image: String,
    val meta: List<String>,
    val metaInformation: List<String>,
    val name: String,
    val original: String,
    val originalName: String,
    val originalString: String,
    val unit: String,
    val unitLong: String,
    val unitShort: String
)

fun List<NetworkRecipeIngredientsItem>.asDomainModel(): List<RecipeIngredients> {
    return map {
        RecipeIngredients(
            id = it.id,
            title = it.title,
            image = it.image,
            imageType = it.imageType,
            likes = it.likes,
            missedIngredientCount = it.missedIngredientCount,
            usedIngredientCount = it.usedIngredientCount,
            unUsedIngredientCount = it.unusedIngredients.count()
        )
    }
}