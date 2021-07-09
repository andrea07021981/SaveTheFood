package com.example.savethefood.shared.data.source.local.entity

import com.example.savethefood.shared.data.domain.BagDomain
import com.example.savethefood.shared.utils.FoodImage
import com.example.savethefood.shared.utils.QuantityType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class BagEntity(
    var id: Int = 0,
    val title: String,
    val img: FoodImage,
    val quantityType: QuantityType,
    val quantity: Double?,
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