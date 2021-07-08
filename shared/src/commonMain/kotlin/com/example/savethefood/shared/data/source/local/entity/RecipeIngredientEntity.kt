package com.example.savethefood.shared.data.source.local.entity

import com.example.savethefood.shared.data.domain.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.example.savethefood.shared.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Serializable
data class RecipeIngredientEntity(
    val recipeId: Long = 0,
    val id: Long,
    val title: String,
    val image: String,
    val imageType:String,
    val likes: Int,
    val missedIngredientCount: Int,
    val usedIngredientCount: Int,
    val unUsedIngredientCount: Int
)

fun RecipeIngredientEntity.asDomainModel(): RecipeIngredients {
    return RecipeIngredients(
        recipeId = recipeId,
        id = id,
        title = title,
        image = image,
        imageType =  "",
        likes = 0,
        missedIngredientCount = 0,
        usedIngredientCount = 0,
        unUsedIngredientCount = 0
    )
}

fun List<RecipeIngredientEntity>.asDomainModel(): List<RecipeIngredients> {
    return map {
        RecipeIngredients(
            recipeId = it.recipeId,
            id = it.id,
            title = it.title,
            image = it.image,
            imageType =  "",
            likes = 0,
            missedIngredientCount = 0,
            usedIngredientCount = 0,
            unUsedIngredientCount = 0
        )
    }
}

/**
 * TODO Workaround: Spoonacular has two different structures for getting a single recipe (in food detail)
 * TODO and REcipeDomain in recipes view, call the api qhen saving in fooddetail and save the RecipeDomain
 */
fun List<RecipeIngredientEntity>.asRecipeDomainModel(): RecipeDomain {
        return RecipeDomain(
            results = this.map {
                RecipeResult(
                    it.recipeId,
                    it.id,
                    "",
                    it.image,
                    "",
                    0,
                    0,
                    it.title
                )
            },
            totalResults = this.count()
        )
}

fun Flow<List<RecipeIngredientEntity>?>.asDomainModel(): Flow<List<RecipeIngredients>?> {
    return map {
        it?.asDomainModel()
    }
}

fun Flow<List<RecipeIngredientEntity>?>.asRecipeDomainModel(): Flow<RecipeDomain?> {
    return map {
        it?.asRecipeDomainModel()
    }
}
