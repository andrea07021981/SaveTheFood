package com.example.savethefood.ui.compose

import android.util.Log
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.savethefood.shared.viewmodel.LoginViewModel
import com.example.savethefood.ui.compose.extention.navigateSafe
import com.example.savethefood.ui.compose.auth.LoginScreen
import com.example.savethefood.ui.compose.auth.SignUpScreen
import com.example.savethefood.ui.compose.navigation.MainNodeDestination
import com.example.savethefood.ui.compose.navigation.MainNodeDestination.ROOT
import com.example.savethefood.ui.compose.navigation.Screen
import com.example.savethefood.ui.compose.pantry.PantryScreen
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import org.koin.androidx.compose.getViewModel

// TODO follow this for the tabrow (the old TabLayout) https://proandroiddev.com/how-to-use-tabs-in-jetpack-compose-41491be61c39
// TODO move into new folder navigation where wee will also have the sealed classes and the navigation manager
@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = MainNodeDestination.AUTH_ROUTE,
) {
    // TODO create a state like AppState for every main screen
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        route = ROOT
    ) {
        addHomeGraph(
            modifier = modifier,
            navController = navController
        )

        addAuthGraph(
            modifier = modifier,
            navController = navController
        )
    }
}

/**
 * Authentication nested graph
 */
fun NavGraphBuilder.addAuthGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    navigation(
        route = MainNodeDestination.AUTH_ROUTE,
        startDestination = Screen.Auth.Login.route
    ) {
        composable(Screen.Auth.Login.route) { from ->
            val viewModel: LoginViewModel = getViewModel()
            LoginScreen(
                modifier = modifier,
                onUserLogged = {
                    navController.navigateSafe(
                        route = MainNodeDestination.HOME_ROUTE,
                        from = from
                    )
                },
                onSignUp = {
                    navController.navigateSafe(Screen.Auth.SignUp.route, from)
                },
                viewModel = viewModel
            )
        }

        composable(Screen.Auth.SignUp.route) { from ->
            val viewModel: LoginViewModel = getViewModel()
            SignUpScreen(
                modifier = modifier,
                onUserLogged = {
                    navController.navigateSafe(
                        route = MainNodeDestination.HOME_ROUTE,
                        from = from
                    )
                },
                onBack = navController::navigateUp,
                viewModel = viewModel
            )
        }
    }
}

/**
 * Add only the composable root navigation for the bottom nav
 */
fun NavGraphBuilder.addHomeGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    // TODO Add nested graphs like addFoodGraph() where we have food and food detail. Inside use FOOD route and the FOOD route/foodId
    // TODO the add button will be declared inside pantry, use state hoisting to open the new food
    // Here we must keep only the events, all the logics, slot apis, etc inside the xScreen
    /**
     * Example for second level nested graph
     * navigation(
    route = HomeSections.FOOD.route,
    startDestination = HomeSections.FOODLIST.route
    ) {
    composable(HomeSections.FOOD.route) { from ->
    PantryScreen(
    onFoodSelected = {
    onSelected(it, from)
    // Here wee probably need to navigate inside the future nested graph
    }
    )
    }
    // TODO Here add the route to the food detail
    }
     */
    navigation(
        route = MainNodeDestination.HOME_ROUTE,
        startDestination = Screen.Home.Food.route
    ) {
        // TODO Use generic func AccountsCard Rally (Use for list of foods as well with generic adapter)
        // TODO we need a generic since th onSeleected could be fooddomain, recipeDomain, etc.. We ecan make onSelected generic and use when is
        composable(Screen.Home.Food.route) { from ->
            PantryScreen(
                onFoodSelected = {
                    // In order to discard duplicated navigation events, we check the Lifecycle
                    //if (from.lifecycleIsResumed()) {
                    // navigate to the specific edit page
                    Log.d("nav", navController.graph.toString())
                    Log.d("Navigation Id selected", id.toString())
                    //TODO use Crossfade to navigate to the details
                    //navController.navigateSafe(route = AuthSections.LOGIN.route, from = from)
                    //}
                }
            )
        }
        composable(Screen.Home.Recipe.route) { from ->
            Food(
                onFoodSelected = {

                },
                modifier = modifier
            ) {
                Text(text = stringResource(Screen.Home.Recipe.title))
            }
        }
        composable(Screen.Home.Bag.route) { from ->
            Text(text = stringResource(Screen.Home.Bag.title))
        }
        composable(Screen.Home.Plan.route) { from ->
            Text(text = stringResource(Screen.Home.Plan.title))
        }
    }
}

@Composable
fun Food(
    modifier: Modifier,
    color: Color = SaveTheFoodTheme.colors.uiBackground,
    contentColor: Color = SaveTheFoodTheme.colors.textSecondary,
    onFoodSelected: (Long) -> Unit,
    content: @Composable () -> Unit
) {
    // This allows to propagate the content color for all the children through the tree
    CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
}
