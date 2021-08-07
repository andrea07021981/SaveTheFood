package com.example.savethefood.shared.data.domain

import com.example.savethefood.shared.Parcelable
import com.example.savethefood.shared.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ProductDomain(
    var id: Int,
    var image: String,
    var imageType: String,
    var title: String
) : Parcelable