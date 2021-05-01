package com.example.savethefood.data.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class RecipeResult(
    val id: Int,
    val baseDomainUrl: String = "",
    val image: String = "",
    val sourceUrl: String?,
    val readyInMinutes: Int = 0,
    val servings: Int = 0,
    val title: String = ""
) : Parcelable {
    constructor() : this(0,"","","",0,0,"")

    constructor(id: Int) : this(id,"","","",0,0,"")
}