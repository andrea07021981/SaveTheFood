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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class RecipeRemoteDataSource @Inject constructor(
    private val foodApi: FoodService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeDataSource{

    @Throws(Exception::class)
    override  fun getRecipes(foodFilter: String?): Flow<RecipeDomain?> = flow {
        try {
            //NEVER USE THE WITHCONTEXT TO CHANGE THE CONTEXT, USE FLOW ON WHICH EXECUTE IN A SECOND THREAD AND CONTEXT
            val recipes = if (foodFilter.isNullOrEmpty()) foodApi.getRecipes() else foodApi.getRecipesByIngredient(foodFilter)
            emit(recipes.asDomainModel())
        } catch (error: Exception) {
            emit(null)
        }
    }

    @Throws(Exception::class)
    override suspend fun getRecipeInfo(id: Int): Result<RecipeInfoDomain> = coroutineScope {
        return@coroutineScope try {
            val recipe = foodApi.getRecipeInfo(id).await()
            // TODO replace result with Apistatus, use result in repository with a converter
            Result.Success(recipe.asDomainModel())
        } catch (error: Exception) {
            Result.ExError(error)
        }
    }

    override suspend fun saveRecipe(recipe: RecipeInfoDomain) {
        //TODO SAVE LOCAL RECIPE
    }
}