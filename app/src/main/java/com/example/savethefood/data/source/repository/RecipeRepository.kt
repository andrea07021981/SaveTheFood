package com.example.savethefood.data.source.repository

import com.example.savethefood.data.source.local.database.SaveTheFoodDatabase
import com.example.savethefood.data.domain.RecipeDomain
import com.example.savethefood.data.domain.RecipeInfoDomain
import com.example.savethefood.data.source.remote.datatransferobject.asDomainModel
import com.example.savethefood.data.source.remote.service.ApiClient
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

class RecipeRepository(
    private val database: SaveTheFoodDatabase
) {

    @Throws(Exception::class)
    suspend fun getRecipes(foodFilter: String?): RecipeDomain = coroutineScope {
        try {
            val recipes = if (foodFilter.isNullOrEmpty()) ApiClient.retrofitService.getRecipes() else ApiClient.retrofitService.getRecipesByIngredient(foodFilter)
            return@coroutineScope recipes.asDomainModel()
        } catch (error: Exception) {
            throw Exception(error)
        }
    }

    @Throws(Exception::class)
    suspend fun getRecipeInfo(id: Int): RecipeInfoDomain = coroutineScope {
        try {
            //TODO Try to get data from db, if not present call api
            val recipe = ApiClient.retrofitService.getRecipeInfo(id).await()
            return@coroutineScope recipe.asDomainModel()
        } catch (error: Exception) {
            throw Exception(error)
        }
    }
}