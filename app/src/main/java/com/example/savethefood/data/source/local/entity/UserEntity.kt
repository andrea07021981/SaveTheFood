package com.example.savethefood.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.savethefood.data.domain.UserDomain

@Entity(tableName = "User")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

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
        email = email,
        password = password
    )
}
