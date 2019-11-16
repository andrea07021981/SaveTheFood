package com.example.savethefood.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.savethefood.local.domain.UserDomain

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0L,

    @ColumnInfo(name = "username")
    val userName: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "password")
    var password: String
)

fun UserEntity.asDomainModel(): UserDomain {
    return UserDomain(
            userName = userName,
            userEmail = email,
            userPassword = password)
}
