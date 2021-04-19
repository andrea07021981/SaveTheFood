package com.example.savethefood.data.source.local.dao

import androidx.room.*
import com.example.savethefood.data.source.local.entity.FoodEntity
import com.example.savethefood.data.source.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow


/**
 * Defines methods for using the entities class with Room.
 */
@Dao
interface RecipeDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: RecipeEntity): Long

    @Transaction
    @Query("SELECT * FROM Recipe WHERE id = :id")
    fun getRecipe(id: Int): RecipeEntity?

    @Transaction
    @Query("SELECT * FROM Recipe")
    fun getRecipes(): Flow<List<RecipeEntity>?>

    @Delete
    fun deleteRecipe(food: RecipeEntity): Int
}
