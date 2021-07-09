package com.example.savethefood.shared.data.source.remote.datatransferobject

import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.data.source.local.entity.FoodEntity
import io.ktor.util.date.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFood(
    val aisle: String?,
    val badges: List<String>,
    val brand: String?,
    val breadcrumbs: List<String?>,
    val description: String?,
    val generatedText: String,
    val id: Int,
    val image: String,
    val imageType: String,
    val images: List<String>,
    val importantBadges: List<String>,
    val ingredientCount: Int,
    val ingredientList: String,
    val ingredients: List<Ingredient>,
    val likes: Int,
    val nutrition: Nutrition,
    val price: Double,
    val servings: Servings,
    val spoonacularScore: Double,
    val title: String,
    val upc: String
)

@Serializable
data class Nutrient(
    val amount: Double,
    val name: String,
    val percentOfDailyNeeds: Double,
    val title: String,
    val unit: String
)

@Serializable
data class Servings(
    val number: Double,
    val size: Double,
    val unit: String
)

@Serializable
data class Nutrition(
    val caloricBreakdown: CaloricBreakdown,
    val calories: Double,
    val carbs: String,
    val fat: String,
    val nutrients: List<Nutrient>,
    val protein: String
)

@Serializable
data class Ingredient(
    val description: String?,
    val name: String,
    @SerialName("safety_level") val safetyLevel: Int?
)

@Serializable
data class CaloricBreakdown(
    val percentCarbs: Double,
    val percentFat: Double,
    val percentProtein: Double
)

// TODO The other properties must be manually selected by the user
fun NetworkFood.asDomainModel(): FoodDomain {
    return FoodDomain(
        id = id.toLong(),
        title = title,
        description = generatedText,
        price = price,
    )
}