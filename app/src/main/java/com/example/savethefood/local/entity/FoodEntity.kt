package com.example.savethefood.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.savethefood.local.domain.FoodDomain

@Entity(tableName = "food_table")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "img_url")
    val imgUrl: String
)

fun FoodEntity.asDomainModel(): FoodDomain {
    return FoodDomain(
        foodId = id,
        foodTitle = title,
        foodDescription = description,
        foodImgUrl = imgUrl
    )
}

fun List<FoodEntity>.asDomainModel(): List<FoodDomain> {
    return map {
        FoodDomain(
            foodId = it.id,
            foodTitle = it.title,
            foodDescription = it.description,
            foodImgUrl = it.imgUrl
        )
    }

}
