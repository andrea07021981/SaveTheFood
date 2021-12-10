package com.example.savethefood.ui.compose

import android.util.Log
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.savethefood.shared.data.domain.UserDomain
import com.example.savethefood.shared.viewmodel.LoginViewModel
import com.example.savethefood.ui.compose.extention.navigateSafe
import com.example.savethefood.ui.compose.auth.LoginScreen
import com.example.savethefood.ui.compose.auth.SignUpScreen
import com.example.savethefood.ui.compose.pantry.PantryScreen
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import org.koin.androidx.compose.getViewModel

// TODO follow this for the tabrow (the old TabLayout) https://proandroiddev.com/how-to-use-tabs-in-jetpack-compose-41491be61c39
// TODO move into new folder navigation where wee will also have the sealed classes and the navigation manager
@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = MainDestinations.AUTH_ROUTE,
) {
    // TODO create a state like AppState for every main screen
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        navigation(
            route = MainDestinations.HOME_ROUTE,
            startDestination = HomeSections.FOOD.route
        ) {
            // TODO Use generic func AccountsCard Rally (Use for list of foods as well with generic adapter)
            // TODO we need a generic since th onSeleected could be fooddomain, recipeDomain, etc.. We ecan make onSelected generic and use when is
            addHomeGraph(
                onSelected = { id: Long, from: NavBackStackEntry ->
                    // In order to discard duplicated navigation events, we check the Lifecycle
                    //if (from.lifecycleIsResumed()) {
                        // navigate to the specific edit page
                        Log.d("Navigation Id selected", id.toString())
                        //TODO use Crossfade to navigate to the details
                        //navController.navigateSafe(route = AuthSections.LOGIN.route, from = from)
                    //}
                },
                modifier = modifier
            )
        }

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
        route = MainDestinations.AUTH_ROUTE,
        startDestination = AuthSections.LOGIN.route
    ) {
        composable(AuthSections.LOGIN.route) { from ->
            val viewModel: LoginViewModel = getViewModel()
            LoginScreen(
                modifier = modifier,
                onUserLogged = {
                    navController.navigateSafe(
                        route = MainDestinations.HOME_ROUTE,
                        from = from
                    )
                },
                onSignUp = {
                    navController.navigateSafe(AuthSections.SIGNUP.route, from)
                },
                viewModel = viewModel
            )
        }

        composable(AuthSections.SIGNUP.route) { from ->
            val viewModel: LoginViewModel = getViewModel()
            SignUpScreen(
                modifier = modifier,
                onUserLogged = {
                    navController.navigateSafe(
                        route = MainDestinations.HOME_ROUTE,
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
    onSelected: (Long, NavBackStackEntry) -> Unit,
    modifier: Modifier = Modifier
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
    composable(HomeSections.FOOD.route) { from ->
        PantryScreen(
            onFoodSelected = {
                onSelected(it, from)
                // Here wee probably need to navigate inside the future nested graph
            }
        )
    }
    composable(HomeSections.RECIPE.route) { from ->
        Food(
            onFoodSelected = { onSelected(it, from) },
            modifier = modifier
        ) {
            Text(text = HomeSections.RECIPE.name)
        }
    }
    composable(HomeSections.BAG.route) { from ->
        Text(text = HomeSections.BAG.name)
    }
    composable(HomeSections.PLAN.route) { from ->
        Text(text = HomeSections.PLAN.name)
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
