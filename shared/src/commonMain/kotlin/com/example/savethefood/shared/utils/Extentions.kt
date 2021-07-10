package com.example.savethefood.shared.utils

import com.example.savethefood.shared.data.ActionResult
import com.example.savethefood.shared.data.domain.FoodDomain

fun Double?.isValidDouble(): Boolean {
    return this != null && this != 0.0
}

fun String.isValidPassword(): Boolean = this.length in 8..16

// TODO find a way to create a generic ext and avoid the repetition of sortedBy
fun List<FoodDomain>.customSortBy(order: FoodOrder): List<FoodDomain> {
    return when (order) {
        FoodOrder.TITLE -> sortedBy(FoodDomain::title)
        FoodOrder.BEFORE -> sortedBy(FoodDomain::bestBefore)
        FoodOrder.QUANTITY -> sortedBy(FoodDomain::quantity)
        else -> this
    }
}

fun <T> List<T>.isListOfNulls(): Boolean = this.all { it == null }

fun <T> List<T>?.getResult(): ActionResult<List<T>> {
    return this?.let {
        if (it.count() > 0) {
            ActionResult.Success(it)
        } else {
            ActionResult.Error("No data")
        }
    } ?: ActionResult.ExError(Exception("Error retrieving data"))
}

fun String.isValidEmail(): Boolean = matches(Regex("^\\S+@\\S+\$"))
