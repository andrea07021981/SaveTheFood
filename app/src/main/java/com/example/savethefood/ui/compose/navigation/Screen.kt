package com.example.savethefood.ui.compose.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.savethefood.R
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.ui.compose.navigation.MainNodeDestination.AUTH_ROUTE
import com.example.savethefood.ui.compose.navigation.MainNodeDestination.HOME_ROUTE

const val ID = "id"
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
    ) : Screen(title, route) {
        object Login : Screen(title = R.string.log_in, route = "$AUTH_ROUTE/login")
        object SignUp : Screen(title = R.string.sign_up, route = "$AUTH_ROUTE/signUp")
    }

    sealed class Home(
        override val title: Int,
        val icon: ImageVector,
        override val route: String
    ) : Screen(title, route) {

        object Food : Home(title = R.string.food, icon = Icons.Outlined.FoodBank, route = "$HOME_ROUTE/food")
        object FoodDetail : Home(title = R.string.food, icon = Icons.Outlined.FoodBank, route = "$HOME_ROUTE/food_detail/{$ID}") {
            val navArguments = listOf(
                navArgument(ID) {
                    type = NavType.LongType
                    defaultValue = -1
                }
            )

            fun navigateToDetail(
                foodId: Long
            ): String {
                return route.replace("{$ID}", "$foodId")
            }
        }
        object Recipe : Home(title = R.string.recipes, icon = Icons.Outlined.Search, route = "$HOME_ROUTE/recipe")
        object Bag : Home(title = R.string.bag, icon = Icons.Outlined.ShoppingCart, route = "$HOME_ROUTE/bag")
        object Plan : Home(title = R.string.plan, icon = Icons.Outlined.AccountCircle, route = "$HOME_ROUTE/plan")
    }
}