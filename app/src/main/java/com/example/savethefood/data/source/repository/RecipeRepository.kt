package com.example.savethefood.data.source.repository

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    @Throws(Exception::class)
    fun getRecipes(foodFilter: String?): Flow<Result<RecipeDomain>>

    @Throws(Exception::class)
    suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain>

    suspend fun saveRecipe(recipe: RecipeInfoDomain)
}