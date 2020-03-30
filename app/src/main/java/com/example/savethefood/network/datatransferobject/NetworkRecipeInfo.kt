package com.example.savethefood.network.datatransferobject

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkRecipeInfo(
    val aggregateLikes: Int,
    val analyzedInstructions: List<AnalyzedInstruction>,
    val cheap: Boolean,
    val cookingMinutes: Int,
    val creditsText: String,
    val cuisines: List<Any>,
    val dairyFree: Boolean,
    val diets: List<String>,
    val dishTypes: List<String>,
    val extendedIngredients: List<ExtendedIngredient>,
    val gaps: String,
    val glutenFree: Boolean,
    val healthScore: Double,
    val id: Int,
    val image: String,
    val imageType: String,
    val instructions: String,
    val lowFodmap: Boolean,
    val occasions: List<Any>,
    val originalId: Any,
    val preparationMinutes: Int,
    val pricePerServing: Double,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceName: String,
    val sourceUrl: String,
    val spoonacularScore: Double,
    val spoonacularSourceUrl: String,
    val summary: String,
    val sustainable: Boolean,
    val title: String,
    val vegan: Boolean,
    val vegetarian: Boolean,
    val veryHealthy: Boolean,
    val veryPopular: Boolean,
    val weightWatcherSmartPoints: Int,
    val winePairing: WinePairing
)

@JsonClass(generateAdapter = true)
data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)

@JsonClass(generateAdapter = true)
data class Equipment(
    val id: Int,
    val image: String,
    val name: String
)

@JsonClass(generateAdapter = true)
data class ExtendedIngredient(
    val aisle: String,
    val amount: Double,
    val consistency: String,
    val id: Int,
    val image: String,
    val measures: Measures,
    val meta: List<String>,
    val metaInformation: List<String>,
    val name: String,
    val original: String,
    val originalName: String,
    val originalString: String,
    val unit: String
)

@JsonClass(generateAdapter = true)
data class Ingredients(
    val id: Int,
    val image: String,
    val name: String
)

@JsonClass(generateAdapter = true)
data class Length(
    val number: Int,
    val unit: String
)

@JsonClass(generateAdapter = true)
data class Measures(
    val metric: Metric,
    val us: Us
)

@JsonClass(generateAdapter = true)
data class Metric(
    val amount: Double,
    val unitLong: String,
    val unitShort: String
)

@JsonClass(generateAdapter = true)
data class Step(
    val equipment: List<Equipment>,
    val ingredients: List<Ingredients>,
    val length: Length,
    val number: Int,
    val step: String
)

@JsonClass(generateAdapter = true)
data class Us(
    val amount: Double,
    val unitLong: String,
    val unitShort: String
)

@JsonClass(generateAdapter = true)
class WinePairing(
)