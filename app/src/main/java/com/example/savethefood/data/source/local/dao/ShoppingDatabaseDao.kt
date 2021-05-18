package com.example.savethefood.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.savethefood.data.source.local.entity.BagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodInBag(item: BagEntity): Long

    @Query("SELECT * FROM Bag")
    fun observeFoodsInBag(): Flow<List<BagEntity>>
}