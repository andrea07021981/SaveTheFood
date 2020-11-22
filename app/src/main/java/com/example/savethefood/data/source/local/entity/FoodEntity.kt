
package com.example.savethefood.data.source.local.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.local.database.FoodImageConverter
import com.example.savethefood.data.source.local.database.StorageTypeConverter
import com.example.savethefood.util.FoodImage
import com.example.savethefood.util.StorageType
import java.sql.Date
import java.util.*

@Entity(tableName = "food_table")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @TypeConverters(FoodImageConverter::class)
    @ColumnInfo(name = "img")
    val img: FoodImage,

    @ColumnInfo(name = "likes")
    val likes: Double?,

    @ColumnInfo(name = "price")
    val price: Double?,

    @ColumnInfo(name = "quantity")
    val quantity: Double?,

    @TypeConverters(StorageTypeConverter::class)
    @ColumnInfo(name = "storage")
    val storageType: StorageType,

    @ColumnInfo(name = "best_before")
    var foodBestBefore: Long
)

fun FoodEntity.asDomainModel(): FoodDomain {
    return FoodDomain(
        foodId = id,
        foodTitle = title,
        foodDescription = description,
        foodImg = img,
        likes = likes,
        price = price,
        quantity = quantity,
        storageType = storageType,
        bestBefore = Date(foodBestBefore)
    )
}

fun List<FoodEntity>.asDomainModel(): List<FoodDomain> {
    return map {
        FoodDomain(
            foodId = it.id,
            foodTitle = it.title,
            foodDescription = it.description,
            foodImg = it.img,
            likes = it.likes,
            price = it.price,
            quantity = it.quantity,
            storageType = it.storageType,
            bestBefore = Date(it.foodBestBefore)
        )
    }

}
