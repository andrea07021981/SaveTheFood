package com.example.savethefood.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_info_table")
data class RecipeInfoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)