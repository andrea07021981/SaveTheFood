package com.example.savethefood.shared.data.domain

import kotlinx.serialization.Serializable

@Serializable
data class ProductDomain(
    var id: Int,
    var image: String,
    var imageType: String,
    var title: String
)