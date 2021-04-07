package com.example.savethefood.data.source.local.dao

import androidx.room.*
import com.example.savethefood.data.source.local.entity.RecipeEntity


/**
 * Defines methods for using the entities class with Room.
 */
@Dao
interface RecipeDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: RecipeEntity): Long

    @Transaction
    @Query("SELECT * FROM Recipe WHERE id = :id")
    fun getRecipe(id: Int): RecipeEntity
}
