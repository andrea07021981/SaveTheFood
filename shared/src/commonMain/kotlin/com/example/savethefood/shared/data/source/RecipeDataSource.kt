package com.example.savethefood.shared.data.source

import com.example.savethefood.shared.data.Result
import com.example.savethefood.shared.data.domain.RecipeDomain
import com.example.savethefood.shared.data.domain.RecipeInfoDomain
import com.example.savethefood.shared.data.domain.RecipeIngredients
import kotlinx.coroutines.flow.Flow

interface RecipeDataSource {

    val tag: String
        get() = RecipeDataSource::class.simpleName!!

    @Throws(Exception::class)
    fun getRecipes(): Flow<RecipeDomain?>

    @Throws(Exception::class)
    suspend fun getRecipeById(id: Int): RecipeDomain?

    @Throws(Exception::class)
    fun getRecipesByIngredients(vararg foodFilter: String?): Flow<List<RecipeIngredients>?>

    @Throws(Exception::class)
    suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain>

    @Throws(Exception::class)
    suspend fun saveRecipe(recipe: RecipeIngredients): Long?

    suspend fun getRecipeIngredients(recipeId: Int): RecipeIngredients?

    fun getRecipesIngredients(): Flow<List<RecipeIngredients>?>

    @Throws(Exception::class)
    suspend fun deleteRecipe(recipeId: RecipeIngredients): Long?

    suspend fun initSession(url: String): Any

    suspend fun closeSession()

    suspend fun observeRecipesStream(): Flow<RecipeDomain>
}