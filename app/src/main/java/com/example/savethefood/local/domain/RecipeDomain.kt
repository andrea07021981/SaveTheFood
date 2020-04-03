package com.example.savethefood.local.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipeDomain(
    val baseUri: String,
    val expires: Long,
    val isStale: Boolean,
    val number: Int,
    val offset: Int,
    val processingTimeMs: Int,
    val results: List<RecipeResult>?,
    val totalResults: Int
) : Parcelable {
    constructor() : this("",0,false,0,0,0, null,0)
}