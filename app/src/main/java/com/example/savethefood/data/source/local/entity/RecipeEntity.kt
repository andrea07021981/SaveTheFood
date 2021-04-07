package com.example.savethefood.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.savethefood.data.domain.RecipeIngredients

// TODO create the complex ER relations, the entity must be like NetworkRecipeInfo
@Entity(tableName = "Recipe")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val image: String
)

fun RecipeEntity.asDomainModel(): RecipeIngredients {
    return RecipeIngredients(
        id = id,
        title = title,
        image = image,
        imageType =  "",
        likes = 0,
        missedIngredientCount = 0,
        usedIngredientCount = 0,
        unUsedIngredientCount = 0
    )
}
