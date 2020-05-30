package com.example.savethefood.data.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FoodSearchDomain(
    var number: Int,
    var offset: Int,
    var products: List<ProductDomain>,
    var totalProducts: Int,
    var type: String
) : Parcelable {
    constructor() : this(0,0, listOf(),0,"")
}