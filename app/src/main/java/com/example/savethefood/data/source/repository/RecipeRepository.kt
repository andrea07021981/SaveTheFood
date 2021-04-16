package com.example.savethefood.data.source.repository

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.data.domain.RecipeResult
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    @Throws(Exception::class)
    fun getRecipes(): Flow<Result<RecipeDomain>>

    @Throws(Exception::class)
    fun getRecipesByIngredients(vararg foodFilter: String?): Flow<Result<List<RecipeIngredients>?>>

    @Throws(Exception::class)
    suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain>

    suspend fun saveRecipe(recipe: RecipeIngredients): Result<RecipeIngredients?>
}