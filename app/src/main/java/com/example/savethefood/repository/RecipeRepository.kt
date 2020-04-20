package com.example.savethefood.repository

import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.RecipeDomain
import com.example.savethefood.local.domain.RecipeInfoDomain
import com.example.savethefood.network.datatransferobject.asDomainModel
import com.example.savethefood.network.service.ApiClient
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

class RecipeRepository(
    private val database: SaveTheFoodDatabase
) {

    @Throws(Exception::class)
    suspend fun getRecipes(foodFilter: String?): RecipeDomain = coroutineScope {
        try {
            val recipes = if (foodFilter.isNullOrEmpty()) ApiClient.retrofitService.getRecipes() else ApiClient.retrofitService.getRecipesByIngredient(foodFilter)
            recipes.asDomainModel()
        } catch (error: Exception) {
            throw Exception(error)
        }
    }

    @Throws(Exception::class)
    suspend fun getRecipeInfo(id: Int): RecipeInfoDomain = coroutineScope {
        try {
            //TODO Try to get data from db, if not present call api
            val recipe = ApiClient.retrofitService.getRecipeInfo(id).await()
            recipe.asDomainModel()
        } catch (error: Exception) {
            throw Exception(error)
        }
    }
}