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
    fun insert(user: UserEntity) : Long

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param user new value to write
     */
    @Update
    fun update(user: UserEntity)

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM user_table")
    fun clear()

    /**
     * Selects and returns the user with given userId.
     */
    @Query("SELECT * from user_table WHERE userId = :key")
    fun getUserWithId(key: Long): LiveData<UserEntity>

    /**
     * Selects and returns the user with given email and pass.(IMP: MUST BE SUSPENDED IN ORDER TO WORK WITH COROUTINES AND FLOW)
     */
    @Query("SELECT * from user_table WHERE email = :userEmail AND password = :userPassword")
    fun getUser(userEmail: String, userPassword: String): Flow<UserEntity?>
}

