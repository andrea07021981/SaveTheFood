package com.example.savethefood.shared.data.source.repository

import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.RecipeDomain
import com.example.savethefood.shared.data.domain.RecipeInfoDomain
import com.example.savethefood.shared.data.domain.RecipeIngredients
import com.example.savethefood.shared.data.domain.RecipeResult
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    @Throws(Exception::class)
    fun getRecipes(): Flow<Result<List<RecipeResult>?>>

    @Throws(Exception::class)
    fun getRecipesByIngredients(vararg foodFilter: String?): Flow<Result<List<RecipeIngredients>?>>

    @Throws(Exception::class)
    suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain>

    suspend fun saveRecipe(recipe: RecipeIngredients): Result<RecipeIngredients?>

    suspend fun initSession(url: String = ""): Result<Unit>

    suspend fun closeSession()

    suspend fun observeStreamRecipes(): Flow<Result<RecipeDomain>>
}