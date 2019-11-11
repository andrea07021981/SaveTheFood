package com.example.savethefood.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.savethefood.local.domain.Food
import com.example.savethefood.local.domain.User

@Entity(tableName = "food_table")
data class FoodEntity(@PrimaryKey(autoGenerate = true)
                      var id: Long = 0L,

                      @ColumnInfo(name = "name")
                      val name: String,

                      @ColumnInfo(name = "img_url")
                      val imgUrl: String
)

fun FoodEntity.asDomainModel(): Food {
    return Food(
        foodId = id,
        foodName = name,
        foodImgUrl = imgUrl
    )
}

fun List<FoodEntity>.asDomainModel(): List<Food> {
    return map {
        Food(
            foodId = it.id,
            foodName = it.name,
            foodImgUrl = it.imgUrl
        )
    }

}
