package com.example.savethefood.ui.compose

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.savethefood.R

enum class HomeSections(
    @StringRes
    val title: Int,
    val icon: ImageVector,
    val route: String
) {
    FOOD(R.string.food, Icons.Outlined.FoodBank, "home/food"),
    RECIPE(R.string.recipes, Icons.Outlined.Search, "home/recipe"),
    BAG(R.string.bag, Icons.Outlined.ShoppingCart, "home/bag"),
    PLAN(R.string.plan, Icons.Outlined.AccountCircle, "home/plan")
}