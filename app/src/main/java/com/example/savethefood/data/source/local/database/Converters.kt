package com.example.savethefood.data.source.local.database

import androidx.room.TypeConverter
import com.example.savethefood.util.FoodImage
import com.example.savethefood.util.QuantityType
import com.example.savethefood.util.StorageType

// TODO add generic class and replace these
class FoodImageConverter{

    @TypeConverter
    fun fromFoodImage(value: FoodImage): String{
        return value.id
    }

    @TypeConverter
    fun toFoodImage(value: String): FoodImage =
        FoodImage.values().find { it.id == value} ?: FoodImage.EMPTY
}

class StorageTypeConverter{

    @TypeConverter
    fun fromStorageType(value: StorageType): String {
        return value.type
    }

    @TypeConverter
    fun toStorageType(value: String): StorageType = when(value){
        "Fridge" -> StorageType.FRIDGE
        "Freezer" -> StorageType.FREEZER
        "Dry" -> StorageType.DRY
        else -> StorageType.UNKNOWN
    }
}

class QuantityTypeConverter{

    @TypeConverter
    fun fromQuantityType(value: QuantityType): String {
        return value.type
    }

    @TypeConverter
    fun toQuantityType(value: String): QuantityType = when(value){
        "Weight" -> QuantityType.WEIGHT
        else -> QuantityType.UNIT
    }
}