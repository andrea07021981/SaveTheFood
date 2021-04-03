package com.example.savethefood.data.source

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.domain.RecipeIngredients
import kotlinx.coroutines.flow.Flow

interface RecipeDataSource {
    @Throws(Exception::class)
    fun getRecipes(): Flow<RecipeDomain?>

    @Throws(Exception::class)
    fun getRecipesByIngredients(vararg foodFilter: String?): Flow<List<RecipeIngredients>?>

    @Throws(Exception::class)
    suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain>

    suspend fun saveRecipe(recipe: RecipeInfoDomain)
}