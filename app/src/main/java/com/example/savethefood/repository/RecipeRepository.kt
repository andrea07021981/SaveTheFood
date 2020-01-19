package com.example.savethefood.repository

import androidx.lifecycle.LiveData
import com.example.savethefood.local.dao.FoodDatabaseDao
import com.example.savethefood.local.database.SaveTheFoodDatabase
import com.example.savethefood.local.domain.RecipeDomain
import com.example.savethefood.network.datatransferobject.AsDomainModel
import com.example.savethefood.network.service.FoodApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.lang.Exception

class RecipeRepository(
    private val database: SaveTheFoodDatabase
) {

    @Throws(Exception::class)
    suspend fun getRecipes(): RecipeDomain = coroutineScope {
        try {
            val recipes = FoodApi.retrofitService.getRecipes().await()
            recipes.AsDomainModel()
        } catch (error: Exception) {
            throw Exception(error)
        }
    }
}