package com.example.savethefood.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.savethefood.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow


/**
 * Defines methods for using the entities class with Room.
 */
@Dao
interface UserDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity) : Long

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param user new value to write
     */
    @Update
    suspend fun update(user: UserEntity)

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM User")
    suspend fun clear()

    /**
     * Selects and returns the user with given id.
     */
    @Query("SELECT * from User WHERE id = :key")
    suspend fun getUserWithId(key: Long): UserEntity?

    /**
     * Selects and returns the user with given email and pass.(IMP: MUST BE SUSPENDED IN ORDER TO WORK WITH COROUTINES AND FLOW)
     */
    @Query("SELECT * from User WHERE email = :userEmail AND password = :userPassword")
    suspend fun getUser(userEmail: String, userPassword: String): UserEntity?
}

