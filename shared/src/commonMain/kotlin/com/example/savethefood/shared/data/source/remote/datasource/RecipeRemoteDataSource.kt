package com.example.savethefood.shared.data.source.remote.datasource

import com.example.savethefood.shared.data.ActionResult
import com.example.savethefood.shared.data.domain.RecipeDomain
import com.example.savethefood.shared.data.domain.RecipeInfoDomain
import com.example.savethefood.shared.data.domain.RecipeIngredients
import com.example.savethefood.shared.data.source.RecipeDataSource
import com.example.savethefood.shared.data.source.remote.datatransferobject.asDomainModel
import com.example.savethefood.shared.data.source.remote.service.FoodServiceApi
import com.example.savethefood.shared.utils.Logger
import com.example.savethefood.shared.utils.isListOfNulls
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class RecipeRemoteDataSource(
    private var client: FoodServiceApi = FoodServiceApi()
) : RecipeDataSource {

    @Throws(Exception::class)
    override fun getRecipes(): Flow<RecipeDomain?> = flow {
        try {
            val recipes = client.getRecipes()
            emit(recipes.asDomainModel())
        } catch (error: Exception) {
            Logger.log(tag, error.message ?: "No message")
            throw error
        }
    }

    override suspend fun getRecipeById(id: Int): RecipeDomain? {
        try {
            val recipe = client.getRecipesById(id = id)
            delay(10000)
            return recipe?.let {
                it.asDomainModel()
            }
        } catch (error: Exception) {
            Logger.log(tag, error.message ?: "No Message")
            throw error
        }
    }

    @Throws(Exception::class)
    override fun getRecipesByIngredients(vararg foodFilter: String?): Flow<List<RecipeIngredients>?>  = flow {
        try {
            //NEVER USE THE WITHCONTEXT TO CHANGE THE CONTEXT, USE FLOW ON WHICH EXECUTE IN A SECOND THREAD AND CONTEXT
            val recipes = if (foodFilter.toList().isListOfNulls()) {
                client.getRecipesByIngredient(null)
            } else {
                client.getRecipesByIngredient(foodFilter.joinToString(","))
            }
            emit(recipes.asDomainModel())
        } catch (error: Exception) {
            Logger.log(tag, error.message ?: "No Message")
            throw error
        }
    }

    @Throws(Exception::class)
    override suspend fun getRecipeInfo(id: Int): ActionResult<RecipeInfoDomain> = coroutineScope {
        return@coroutineScope try {
            val recipe = client.getRecipeInfo(id)
            ActionResult.Success(recipe.asDomainModel())
        } catch (error: Exception) {
            ActionResult.ExError(error)
        }
    }

    override suspend fun saveRecipe(recipe: RecipeIngredients): Long? {
        return null
    }

    override suspend fun getRecipeIngredients(recipeId: Int): RecipeIngredients? {
        return null
    }

    override fun getRecipesIngredients(): Flow<List<RecipeIngredients>?> {
        return flow {  }
    }

    override suspend fun deleteRecipe(recipeId: RecipeIngredients): Long? {
        return null
    }
}
