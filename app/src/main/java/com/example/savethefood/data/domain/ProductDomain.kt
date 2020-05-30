package com.example.savethefood.data.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDomain(
    var id: Int,
    var image: String,
    var imageType: String,
    var title: String
) : Parcelable {
    constructor() : this(0,"","","")
}