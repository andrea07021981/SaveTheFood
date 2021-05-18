package com.example.savethefood.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.savethefood.data.domain.BagDomain
import com.example.savethefood.data.domain.RecipeIngredients
import com.google.android.material.badge.BadgeDrawable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Entity(tableName = "Bag")
data class BagEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "quantity")
    val quantity: Double,

)

fun List<BagEntity>.asDomainModel(): List<BagDomain> {
    return map {
        BagDomain(
            id = it.id,
            title = it.title,
            quantity = it.quantity
        )
    }
}

fun Flow<List<BagEntity>>.asDomainModel(): Flow<List<BagDomain>?> {
    return map {
        it.asDomainModel()
    }
}