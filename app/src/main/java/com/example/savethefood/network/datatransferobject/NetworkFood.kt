package com.example.savethefood.network.datatransferobject

import com.example.savethefood.local.domain.Food
import com.example.savethefood.local.entity.FoodEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Convert Network results to database objects
 */

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
    val likes: Double?,
    @Json(name = "number_of_servings")val numberOfServings: Double?,
    val nutrition: Nutrition?,
    val price: Double?,
    @Json(name = "serving_size")val servingSize: String?,
    @Json(name = "spoonacular_score")val spoonacularScore: Double?,
    val title: String
)

@JsonClass(generateAdapter = true)
data class Ingredient(
    val description: Any?,
    val name: String?,
    @Json(name = "safety_level")val safetyLevel: Any?
)

@JsonClass(generateAdapter = true)
data class Nutrition(
    val calories: Double?,
    val carbs: String?,
    val fat: String?,
    val protein: String?
)


//**
// Convert Network results to database objects
//*
fun NetworkFood.asDomainModel(): Food {
    return Food(
        foodId = id,
        foodName = title,
        foodImgUrl = images.first())
}

fun NetworkFood.asDatabaseModel(): FoodEntity {
    return FoodEntity(
        id = id,
        name = title,
        imgUrl = images.first()
    )
}