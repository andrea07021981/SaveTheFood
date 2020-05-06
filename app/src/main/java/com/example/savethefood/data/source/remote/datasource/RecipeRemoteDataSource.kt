package com.example.savethefood.data.source.remote.datasource

import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.source.RecipeDataSource
import com.example.savethefood.data.source.remote.datatransferobject.asDomainModel
import com.example.savethefood.data.source.remote.service.ApiClient
import com.example.savethefood.data.source.remote.service.FoodService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

class RecipeRemoteDataSource(
    private val foodApi: FoodService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeDataSource{

    @Throws(Exception::class)
    override suspend fun getRecipes(foodFilter: String?): RecipeDomain = coroutineScope {
        try {
            val recipes = if (foodFilter.isNullOrEmpty()) foodApi.getRecipes() else foodApi.getRecipesByIngredient(foodFilter)
            return@coroutineScope recipes.asDomainModel()
        } catch (error: Exception) {
            throw Exception(error)
        }
    }

    @Throws(Exception::class)
    override suspend fun getRecipeInfo(id: Int): RecipeInfoDomain = coroutineScope {
        try {
            val recipe = foodApi.getRecipeInfo(id).await()
            return@coroutineScope recipe.asDomainModel()
        } catch (error: Exception) {
            throw Exception(error)
        }
    }

    override suspend fun saveRecipe(recipe: RecipeInfoDomain) {
        //TODO SAVE LOCAL RECIPE
    }
}