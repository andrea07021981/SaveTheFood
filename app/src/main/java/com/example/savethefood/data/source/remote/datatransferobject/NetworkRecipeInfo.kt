package com.example.savethefood.data.source.remote.datatransferobject

import com.example.savethefood.data.domain.*
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkRecipeInfo(
    val aggregateLikes: Int,
    val analyzedInstructions: List<AnalyzedInstruction>,
    val cheap: Boolean,
    val cookingMinutes: Int?,
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
    val instructions: String?,
    val lowFodmap: Boolean,
    val occasions: List<String>,
    val originalId: Int?,
    val preparationMinutes: Int?,
    val pricePerServing: Double,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceName: String,
    val sourceUrl: String,
    val spoonacularScore: Double,
    val spoonacularSourceUrl: String?,
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

fun NetworkRecipeInfo.asDomainModel(): RecipeInfoDomain {
    return RecipeInfoDomain(
        recipeAggregateLikes = aggregateLikes,
        recipeAnalyzedInstructions = analyzedInstructions.map { it.asDomainModel() },
        recipeCheap = cheap,
        recipeCookingMinutes = cookingMinutes,
        recipeCreditsText = creditsText,
        recipeCuisines = cuisines.map { it.toString() },
        recipeDairyFree = dairyFree,
        recipeDiets = diets,
        recipeDishTypes = dishTypes,
        recipeExtendedIngredients = extendedIngredients.map { it.asDomainModel() },
        recipeGaps = gaps,
        recipeGlutenFree = glutenFree,
        recipeHealthScore = healthScore,
        recipeId = id,
        recipeImage = image,
        recipeImageType = imageType,
        recipeInstructions = instructions,
        recipeLowFodmap = lowFodmap,
        recipeOccasions = occasions,
        recipeOriginalId = originalId,
        recipePreparationMinutes = preparationMinutes,
        recipePricePerServing = pricePerServing,
        recipeReadyInMinutes = readyInMinutes,
        recipeRecipeServings = servings,
        recipeSourceName = sourceName,
        recipeSourceUrl = sourceUrl,
        recipeSpoonacularScore = spoonacularScore,
        recipeSpoonacularSourceUrl = spoonacularSourceUrl,
        recipeSummary = summary,
        recipeSustainable = sustainable,
        recipeTitle = title,
        recipeVegan = vegan,
        recipeVegetarian = vegetarian,
        recipeVeryHealthy = veryHealthy,
        recipeVeryPopular = veryPopular,
        recipeWeightWatcherSmartPoints = weightWatcherSmartPoints,
        recipeWinePairing = winePairing.asDomainModel()
    )
}

@JsonClass(generateAdapter = true)
data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)

fun AnalyzedInstruction.asDomainModel(): AnalyzedInstructionDomain {
    return AnalyzedInstructionDomain(
        instructionName = name,
        instructionSteps = steps.map { it.asDomainModel() }
    )
}

@JsonClass(generateAdapter = true)
data class Equipment(
    val id: Int,
    val image: String,
    val name: String
)

fun Equipment.asDomainModel(): EquipmentDomain {
    return EquipmentDomain(
        equipmentId = id,
        equipmentImage = image,
        equipmentName = name
    )
}

@JsonClass(generateAdapter = true)
data class ExtendedIngredient(
    val aisle: String?,
    val amount: Double?,
    val consistency: String?,
    val id: Int?,
    val image: String?,
    val measures: Measures?,
    val meta: List<String>,
    val metaInformation: List<String>,
    val name: String?,
    val original: String?,
    val originalName: String?,
    val originalString: String?,
    val unit: String?
)

fun ExtendedIngredient.asDomainModel(): ExtendedIngredientDomain {
    return ExtendedIngredientDomain(
        exIngredientAisle = aisle,
        exIngredientAmount = amount,
        exIngredientConsistency = consistency,
        exIngredientId = id,
        exIngredientImage = image,
        exIngredientMeasures = measures?.asDomainModel(),
        exIngredientMeta = meta,
        exIngredientMetaInformation = metaInformation,
        exIngredientName = name,
        exIngredientOriginal = original,
        exIngredientOriginalName = originalName,
        exIngredientOriginalString = originalString,
        exIngredientUnit = unit
    )
}

@JsonClass(generateAdapter = true)
data class Ingredients(
    val id: Int,
    val image: String,
    val name: String
)
fun Ingredients.asDomainModel(): IngredientsDomain {
    return IngredientsDomain(
        ingredientId = id,
        ingredientImage = image,
        ingredientName = name
    )
}

@JsonClass(generateAdapter = true)
data class Length(
    val number: Int,
    val unit: String
)

fun Length.asDomainModel(): LengthDomain {
    return LengthDomain(
        lengthNumber = number,
        lengthUnit = unit
    )
}

@JsonClass(generateAdapter = true)
data class Measures(
    val metric: Metric,
    val us: Us
)

fun Measures.asDomainModel(): MeasuresDomain {
    return MeasuresDomain(
        measureMetric = metric.asDomainModel(),
        measureUs = us.asDomainModel()
    )
}

@JsonClass(generateAdapter = true)
data class Metric(
    val amount: Double,
    val unitLong: String,
    val unitShort: String
)
fun Metric.asDomainModel(): MetricDomain {
    return MetricDomain(
        metricAmount = amount,
        metricUnitLong = unitLong,
        metricUnitShort = unitShort
    )
}

@JsonClass(generateAdapter = true)
data class Step(
    val equipment: List<Equipment>,
    val ingredients: List<Ingredients>,
    val length: Length?,
    val number: Int,
    val step: String
)

fun Step.asDomainModel(): StepDomain {
    return StepDomain(
        stepEquipment = equipment.map { it.asDomainModel() },
        stepIngredients = ingredients.map { it.asDomainModel() },
        stepLength = length?.asDomainModel(),
        stepNumber = number,
        stepStep = step
    )
}

@JsonClass(generateAdapter = true)
data class Us(
    val amount: Double,
    val unitLong: String,
    val unitShort: String
)
fun Us.asDomainModel(): UsDomain {
    return UsDomain(
        amount = amount,
        unitLong = unitLong,
        unitShort = unitShort
    )
}

@JsonClass(generateAdapter = true)
class WinePairing(
)

fun WinePairing.asDomainModel(): WinePairingDomain {
    return WinePairingDomain()
}
/**
 * Convert Network results to domain objects
 */

/*fun AnalyzedInstruction.asDomainModel(): List<AnalyzedInstructionDomain> {
    return AnalyzedInstructionDomain(
        instructionName = name,
        instructionSteps = steps
    )
}

fun Step.asDomainModel(): List<StepDomain> {
    return step.map {
        StepDomain(
            stepEquipment =  it.
        )
    }

}

/*
fun NetworkRecipeInfo.asDomainModel(): RecipeInfoDomain {
    return RecipeInfoDomain(
        recipeAggregateLikes = aggregateLikes,
    val recipeAnalyzedInstructions: List<AnalyzedInstructionDomain>,
    val recipeCheap: Boolean,
    val recipeCookingMinutes: Int,
    val recipeCreditsText: String,
    val recipeCuisines: List<String>,
    val recipeCairyFree: Boolean,
    val recipeDiets: List<String>,
    val recipeDishTypes: List<String>,
    val recipeExtendedIngredients: List<ExtendedIngredientDomain>,
    val recipeGaps: String,
    val recipeGlutenFree: Boolean,
    val recipeHealthScore: Double,
    val recipeId: Int,
    val recipeImage: String,
    val recipeImageType: String,
    val recipeInstructions: String,
    val recipeLowFodmap: Boolean,
    val recipeOccasions: List<String>,
    val recipeOriginalId: Int,
    val recipePreparationMinutes: Int,
    val recipePricePerServing: Double,
    val recipeReadyInMinutes: Int,
    val recipeRecipeServings: Int,
    val recipeSourceName: String,
    val recipeSourceUrl: String,
    val recipeSpoonacularScore: Double,
    val recipeSpoonacularSourceUrl: String,
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
}*/