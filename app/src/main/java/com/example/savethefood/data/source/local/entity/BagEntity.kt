package com.example.savethefood.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.savethefood.constants.QuantityType
import com.example.savethefood.data.domain.BagDomain
import com.example.savethefood.data.domain.RecipeIngredients
import com.example.savethefood.data.source.local.database.FoodImageConverter
import com.example.savethefood.data.source.local.database.QuantityTypeConverter
import com.example.savethefood.util.FoodImage
import com.google.android.material.badge.BadgeDrawable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Entity(tableName = "Bag")
data class BagEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @TypeConverters(FoodImageConverter::class)
    @ColumnInfo(name = "img")
    val img: FoodImage,

    @TypeConverters(QuantityTypeConverter::class)
    @ColumnInfo(name = "quantity_type")
    val quantityType: QuantityType?,

    @ColumnInfo(name = "quantity")
    val quantity: Double,

    )

fun List<BagEntity>.asDomainModel(): List<BagDomain> {
    return map {
        BagDomain(
            id = it.id,
            title = it.title,
            img = it.img,
            quantityType = it.quantityType,
            quantity = it.quantity
        )
    }
}

fun Flow<List<BagEntity>>.asDomainModel(): Flow<List<BagDomain>?> {
    return map {
        it.asDomainModel()
    }
}