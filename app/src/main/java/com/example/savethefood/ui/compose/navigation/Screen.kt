package com.example.savethefood.ui.compose.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.savethefood.R
import com.example.savethefood.ui.compose.navigation.MainNodeDestination.AUTH_ROUTE
import com.example.savethefood.ui.compose.navigation.MainNodeDestination.HOME_ROUTE

/**
 * TODO add the @Compose param like Rally screen and also pass the navcontroller. We probably need to change some object to class
 * OR add the @Composable with high order and destination like Rally
 */
sealed class Screen(
    @StringRes
    open val title: Int,
    open val route: String
) {

    sealed class Auth(
        override val title: Int,
        override val route: String
    ) : Screen(title, "$AUTH_ROUTE/$route") {
        object Login : Screen(title = R.string.log_in, route = "login")
        object SignUp : Screen(title = R.string.sign_up, route = "signUp")
    }

    sealed class Home(
        override val title: Int,
        val icon: ImageVector,
        override val route: String
    ) : Screen(title, "$HOME_ROUTE/$route") {
        object Food : Home(title = R.string.food, icon = Icons.Outlined.FoodBank, route = "food")
        object Recipe : Home(title = R.string.recipes, icon = Icons.Outlined.Search, route = "recipe")
        object Bag : Home(title = R.string.bag, icon = Icons.Outlined.ShoppingCart, route = "bag")
        object Plan : Home(title = R.string.plan, icon = Icons.Outlined.AccountCircle, route = "plan")
    }
}