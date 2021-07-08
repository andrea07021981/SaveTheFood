package com.example.savethefood.shared.data.domain

import kotlinx.serialization.Serializable

@Serializable
data class RecipeInfoDomain(
    val recipeAggregateLikes: Int,
    val recipeAnalyzedInstructions: List<AnalyzedInstructionDomain>,
    val recipeCheap: Boolean,
    val recipeCookingMinutes: Int?,
    val recipeCreditsText: String,
    val recipeCuisines: List<String>,
    val recipeDairyFree: Boolean,
    val recipeDiets: List<String>,
    val recipeDishTypes: List<String>,
    val recipeExtendedIngredients: List<ExtendedIngredientDomain>,
    val recipeGaps: String,
    val recipeGlutenFree: Boolean,
    val recipeHealthScore: Double,
    val recipeId: Int,
    val recipeImage: String,
    val recipeImageType: String,
    val recipeInstructions: String?,
    val recipeLowFodmap: Boolean,
    val recipeOccasions: List<String>,
    val recipeOriginalId: Int?,
    val recipePreparationMinutes: Int?,
    val recipePricePerServing: Double,
    val recipeReadyInMinutes: Int,
    val recipeRecipeServings: Int,
    val recipeSourceName: String,
    val recipeSourceUrl: String,
    val recipeSpoonacularScore: Double,
    val recipeSpoonacularSourceUrl: String?,
    val recipeSummary: String,
    val recipeSustainable: Boolean,
    val recipeTitle: String,
    val recipeVegan: Boolean,
    val recipeVegetarian: Boolean,
    val recipeVeryHealthy: Boolean,
    val recipeVeryPopular: Boolean,
    val recipeWeightWatcherSmartPoints: Int,
    val recipeWinePairing: WinePairingDomain
)

@Serializable
data class AnalyzedInstructionDomain(
    val instructionName: String,
    val instructionSteps: List<StepDomain>
)

@Serializable
data class EquipmentDomain(
    val equipmentId: Int,
    val equipmentImage: String,
    val equipmentName: String
)

@Serializable
data class ExtendedIngredientDomain(
    val exIngredientAisle: String?,
    val exIngredientAmount: Double?,
    val exIngredientConsistency: String?,
    val exIngredientId: Int?,
    val exIngredientImage: String?,
    val exIngredientMeasures: MeasuresDomain?,
    val exIngredientMeta: List<String>,
    val exIngredientMetaInformation: List<String>,
    val exIngredientName: String?,
    val exIngredientOriginal: String?,
    val exIngredientOriginalName: String?,
    val exIngredientOriginalString: String?,
    val exIngredientUnit: String?
)

@Serializable
data class IngredientsDomain(
    val ingredientId: Int,
    val ingredientImage: String,
    val ingredientName: String
)

@Serializable
data class LengthDomain(
    val lengthNumber: Int,
    val lengthUnit: String
)

@Serializable
data class MeasuresDomain(
    val measureMetric: MetricDomain,
    val measureUs: UsDomain
)

@Serializable
data class MetricDomain(
    val metricAmount: Double,
    val metricUnitLong: String,
    val metricUnitShort: String
)

@Serializable
data class StepDomain(
    val stepEquipment: List<EquipmentDomain>,
    val stepIngredients: List<IngredientsDomain>,
    val stepLength: LengthDomain?,
    val stepNumber: Int,
    val stepStep: String
)

@Serializable
data class UsDomain(
    val amount: Double,
    val unitLong: String,
    val unitShort: String
)

@Serializable
class WinePairingDomain(
)