package com.example.savethefood.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO add room data
@Entity(tableName = "RecipeInfo")
data class RecipeInfoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)