package com.example.savethefood.data.source.remote.datatransferobject

import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.local.entity.FoodEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

/**
 * Convert Network results to database objects
 */

@Deprecated("Removed API call for foods")
@JsonClass(generateAdapter = true)
data class NetworkFood(
    val badges: List<String>?,
    @Json(name = "breadcrumbs") val breadCrumbs: List<String>?,
    val generatedText: String?,
    val id: Int,
    val images: List<String>,
    @Json(name = "important_badges")val importantBadges: List<String>?,
    val ingredientCount: Any?,
    val ingredientList: String?,
    val ingredients: List<Ingredient>?,
    @Json(name = "number_of_servings")val numberOfServings: Double?,
    val nutrition: Nutrition?,
    val price: Double?,
    @Json(name = "serving_size")val servingSize: String?,
    @Json(name = "spoonacular_score")val spoonacularScore: Double?,
    val title: String
)
@Deprecated("Removed API call for foods")
@JsonClass(generateAdapter = true)
data class Ingredient(
    val description: Any?,
    val name: String?,
    @Json(name = "safety_level")val safetyLevel: Any?
)
@Deprecated("Removed API call for foods")
@JsonClass(generateAdapter = true)
data class Nutrition(
    val calories: Double?,
    val carbs: String?,
    val fat: String?,
    val protein: String?
)

/*
*//**
* Convert Network results to database objects
*//*
fun NetworkFood.asDomainModel(): FoodDomain {
    return FoodDomain(
        foodId = id,
        foodTitle = title,
        foodDescription = generatedText,
        foodImgUrl = images.first(),
        likes = likes,
        price = price,
        calories = nutrition?.calories,
        fat = nutrition?.fat,
        proteins = nutrition?.protein,
        carbs = nutrition?.carbs,
        ingredientList = ingredientList,
        servingSize = servingSize,
        bestBefore = Date()
    )
}

fun NetworkFood.asDatabaseModel(): FoodEntity {
    return FoodEntity(
        id = id,
        title = title,
        description = generatedText,
        imgUrl = images.first(),
        likes = likes,
        price = price,
        calories = nutrition?.calories,
        fat = nutrition?.fat,
        proteins = nutrition?.protein,
        carbs = nutrition?.carbs,
        ingredientList = ingredientList,
        servingSize = servingSize,
        foodBestBefore = Date().time
    )
}*/