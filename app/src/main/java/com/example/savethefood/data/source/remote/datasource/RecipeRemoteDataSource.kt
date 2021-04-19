package com.example.savethefood.data.source.remote.datasource

import android.util.Log
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.data.domain.RecipeResult
import com.example.savethefood.data.source.RecipeDataSource
import com.example.savethefood.data.source.local.entity.RecipeEntity
import com.example.savethefood.data.source.remote.datatransferobject.asDomainModel
import com.example.savethefood.data.source.remote.service.FoodService
import com.example.savethefood.util.isListOfNulls
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
    override fun getRecipes(): Flow<RecipeDomain?> = flow {
        try {
            val recipes = foodApi.getRecipes()
            emit(recipes.asDomainModel())
        } catch (error: Exception) {
            Log.d("Error", error.localizedMessage)
            throw error
        }
    }

    @Throws(Exception::class)
    override fun getRecipesByIngredients(vararg foodFilter: String?): Flow<List<RecipeIngredients>?>  = flow {
        try {
            //NEVER USE THE WITHCONTEXT TO CHANGE THE CONTEXT, USE FLOW ON WHICH EXECUTE IN A SECOND THREAD AND CONTEXT
            val recipes = if (foodFilter.toList().isListOfNulls()) {
                foodApi.getRecipesByIngredient(null)
            } else {
                foodApi.getRecipesByIngredient(foodFilter.joinToString(","))
            }
            emit(recipes.asDomainModel())
        } catch (error: Exception) {
            Log.d("Error", error.localizedMessage)
            throw error
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

    override suspend fun saveRecipe(recipe: RecipeIngredients): RecipeIngredients? {
        return null
    }

    override suspend fun getRecipeIngredients(recipeId: Int): RecipeIngredients? {
        return null
    }

    override fun getRecipesIngredients(): Flow<List<RecipeIngredients>?> {
        return flow {  }
    }

    override suspend fun deleteRecipe(recipeId: RecipeIngredients): Int? {
        return null
    }
}