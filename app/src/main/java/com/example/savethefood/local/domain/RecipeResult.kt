package com.example.savethefood.local.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class RecipeResult(
    val id: Int,
    val baseDomainUrl: String,
    val image: String,
    val sourceUrl: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val title: String
) : Parcelable {
    constructor() : this(0,"","","",0,0,"")
}