
package com.example.savethefood.data.source.local.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.savethefood.data.domain.FoodDomain

@Entity(tableName = "food_table")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "img_url")
    val imgUrl: String,

    @ColumnInfo(name = "likes")
    val likes: Double?,

    @ColumnInfo(name = "price")
    val price: Double?,

    @ColumnInfo(name = "calories")
    val calories: Double?,

    @ColumnInfo(name = "fat")
    val fat: String?,

    @ColumnInfo(name = "proteins")
    val proteins: String?,

    @ColumnInfo(name = "carbs")
    val carbs: String?,

    @ColumnInfo(name = "ingredients")
    var ingredientList: String?,

    @ColumnInfo(name = "serving_size")
    var servingSize: String?

)

fun FoodEntity.asDomainModel(): FoodDomain {
    return FoodDomain(
        foodId = id,
        foodTitle = title,
        foodDescription = description,
        foodImgUrl = imgUrl,
        likes = likes,
        price = price,
        calories = calories,
        fat = fat,
        proteins = proteins,
        carbs = carbs,
        ingredientList = ingredientList,
        servingSize = servingSize
    )
}

fun List<FoodEntity>.asDomainModel(): List<FoodDomain> {
    return map {
        FoodDomain(
            foodId = it.id,
            foodTitle = it.title,
            foodDescription = it.description,
            foodImgUrl = it.imgUrl,
            likes = it.likes,
            price = it.price,
            calories = it.calories,
            fat = it.fat,
            proteins = it.proteins,
            carbs = it.carbs,
            ingredientList = it.ingredientList,
            servingSize = it.servingSize
        )
    }

}
