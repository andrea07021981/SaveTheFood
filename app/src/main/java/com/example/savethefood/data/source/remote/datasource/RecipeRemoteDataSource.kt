package com.example.savethefood.data.source.remote.datasource

import com.example.savethefood.data.Result
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
    override suspend fun getRecipes(foodFilter: String?): Result<RecipeDomain> = coroutineScope {
        return@coroutineScope try {
            val recipes = if (foodFilter.isNullOrEmpty()) foodApi.getRecipes() else foodApi.getRecipesByIngredient(foodFilter)
            Result.Success(recipes.asDomainModel())
        } catch (error: Exception) {
            Result.ExError(error)
        }
    }

    @Throws(Exception::class)
    override suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain> = coroutineScope {
        return@coroutineScope try {
            val recipe = foodApi.getRecipeInfo(id).await()
            Result.Success(recipe.asDomainModel())
        } catch (error: Exception) {
            Result.ExError(error)
        }
    }

    override suspend fun saveRecipe(recipe: RecipeInfoDomain) {
        //TODO SAVE LOCAL RECIPE
    }
}