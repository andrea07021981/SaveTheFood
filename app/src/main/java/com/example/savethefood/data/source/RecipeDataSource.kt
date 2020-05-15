package com.example.savethefood.data.source

import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain

interface RecipeDataSource {
    @Throws(Exception::class)
    suspend fun getRecipes(foodFilter: String?): Result<RecipeDomain>

    @Throws(Exception::class)
    suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain>

    suspend fun saveRecipe(recipe: RecipeInfoDomain)
}