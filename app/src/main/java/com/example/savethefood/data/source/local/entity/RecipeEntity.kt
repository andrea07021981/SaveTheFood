package com.example.savethefood.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_table")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "baseUri")
    val baseUri: String,

    @ColumnInfo(name = "expires")
    val expires: Long,

    @ColumnInfo(name = "isState")
    val isStale: Boolean,

    @ColumnInfo(name = "number")
    val number: Int,

    @ColumnInfo(name = "offset")
    val offset: Int,

    @ColumnInfo(name = "processingTimeMs")
    val processingTimeMs: Int,

    @ColumnInfo(name = "resultsList")
    val resultsList: String,

    @ColumnInfo(name = "totalResults")
    val totalResults: Int
)