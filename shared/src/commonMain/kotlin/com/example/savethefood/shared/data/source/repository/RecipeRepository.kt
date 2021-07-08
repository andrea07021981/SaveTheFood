package com.example.savethefood.shared.data.source.repository

import com.example.savethefood.shared.data.ActionResult
import com.example.savethefood.shared.data.domain.RecipeInfoDomain
import com.example.savethefood.shared.data.domain.RecipeIngredients
import com.example.savethefood.shared.data.domain.RecipeResult
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    @Throws(Exception::class)
    fun getRecipes(): Flow<ActionResult<List<RecipeResult>?>>

    @Throws(Exception::class)
    fun getRecipesByIngredients(vararg foodFilter: String?): Flow<ActionResult<List<RecipeIngredients>?>>

    @Throws(Exception::class)
    suspend fun getRecipeInfo(id: Int): ActionResult<RecipeInfoDomain>

    suspend fun saveRecipe(recipe: RecipeIngredients): ActionResult<RecipeIngredients?>
}