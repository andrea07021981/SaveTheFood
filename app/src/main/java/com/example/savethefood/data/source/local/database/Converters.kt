package com.example.savethefood.data.source.local.database

import androidx.room.TypeConverter
import com.example.savethefood.constants.QuantityType
import com.example.savethefood.constants.StorageType
import com.example.savethefood.util.FoodImage
import java.util.*

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
        else -> StorageType.ALL
    }
}

class QuantityTypeConverter{

    @TypeConverter
    fun fromQuantityType(value: QuantityType): String {
        return value.type
    }

    @TypeConverter
    fun toQuantityType(value: String): QuantityType = when(value){
        "weight" -> QuantityType.WEIGHT
        else -> QuantityType.UNIT
    }
}

class TimeStampConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}