package com.example.savethefood.repository

import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.RecipeDomain
import com.example.savethefood.network.datatransferobject.asDomainModel
import com.example.savethefood.network.service.FoodApi
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

class RecipeRepository(
    private val database: SaveTheFoodDatabase
) {

    @Throws(Exception::class)
    suspend fun getRecipes(): RecipeDomain = coroutineScope {
        try {
            val recipes = FoodApi.retrofitService.getRecipes().await()
            recipes.asDomainModel()
        } catch (error: Exception) {
            throw Exception(error)
        }
    }

    /*@Throws(Exception::class)
    suspend fun getRecipeInfo(val id: Int): RecipeDomain*/
}