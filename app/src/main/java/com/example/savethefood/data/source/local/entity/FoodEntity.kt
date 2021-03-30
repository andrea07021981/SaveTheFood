
package com.example.savethefood.data.source.local.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.source.local.database.FoodImageConverter
import com.example.savethefood.data.source.local.database.QuantityTypeConverter
import com.example.savethefood.data.source.local.database.StorageTypeConverter
import com.example.savethefood.util.FoodImage
import com.example.savethefood.constants.QuantityType
import com.example.savethefood.constants.StorageType
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

    @ColumnInfo(name = "price")
    val price: Double?,

    @TypeConverters(QuantityTypeConverter::class)
    @ColumnInfo(name = "quantity_type")
    val quantityType: QuantityType,

    @ColumnInfo(name = "quantity")
    val quantity: Double?,

    @TypeConverters(StorageTypeConverter::class)
    @ColumnInfo(name = "storage")
    val storageType: StorageType,

    @ColumnInfo(name = "best_before")
    var foodBestBefore: Date,

    @ColumnInfo(name = "last_update")
    var lastUpdate: Date
)

fun FoodEntity.asDomainModel(): FoodDomain {
    return FoodDomain(
        id = id,
        title = title,
        description = description,
        img = img,
        price = price,
        quantityType = quantityType,
        quantity = quantity,
        storageType = storageType,
        bestBefore = foodBestBefore,
        lastUpdate = lastUpdate
    )
}

fun List<FoodEntity>.asDomainModel(): List<FoodDomain> {
    return map {
        FoodDomain(
            id = it.id,
            title = it.title,
            description = it.description,
            img = it.img,
            price = it.price,
            quantityType = it.quantityType,
            quantity = it.quantity,
            storageType = it.storageType,
            bestBefore = it.foodBestBefore,
            lastUpdate = it.lastUpdate
        )
    }

}
