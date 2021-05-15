package com.example.savethefood.data.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class RecipeResult(
    val recipeId: Long = 0, // Id to distinguish the local and remote recipe
    val id: Int,
    val baseDomainUrl: String = "",
    val image: String = "",
    val sourceUrl: String?,
    val readyInMinutes: Int = 0,
    val servings: Int = 0,
    val title: String = ""
) : Parcelable {
    constructor() : this(0L,0,"","","",0,0,"")

    constructor(id: Int) : this(0L, id,"","","",0,0,"")
}