package com.example.savethefood.ui.compose

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.savethefood.R
import com.example.savethefood.shared.data.domain.FoodDomain

// TODO use same as Rally screen but inside body add everything. Need to add navcontroller in constructor line 56
// TODO we should use sealed class, we can pass data through constructors dynamically (ENUM is static)
enum class HomeSections(
    @StringRes
    val title: Int,
    val icon: ImageVector,
    val route: String
) {
    FOOD(R.string.food, Icons.Outlined.FoodBank, "home/food"), // TODO replace with pantry, so wee will have home/pantry/foods and home/pantry/foods/{id} for details
    RECIPE(R.string.recipes, Icons.Outlined.Search, "home/recipe"),
    BAG(R.string.bag, Icons.Outlined.ShoppingCart, "home/bag"),
    PLAN(R.string.plan, Icons.Outlined.AccountCircle, "home/plan")
}