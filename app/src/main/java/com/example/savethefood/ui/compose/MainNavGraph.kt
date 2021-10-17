package com.example.savethefood.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.savethefood.shared.data.domain.FoodDomain

// TODO follow this for the tabrow (the old TabLayout) https://proandroiddev.com/how-to-use-tabs-in-jetpack-compose-41491be61c39

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.HOME_ROUTE,
) {
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
            addHomeGraph(
                onSelected = { id: Long, from: NavBackStackEntry ->
                    // In order to discard duplicated navigation events, we check the Lifecycle
                    if (from.lifecycleIsResumed()) {
                        // navigate to the specific edit page
                    }
                },
                modifier = modifier
            )
        }
        // TODO add the other views here lik eedit food, add recipe, etc
    }
}

fun NavGraphBuilder.addHomeGraph(
    onSelected: (Long, NavBackStackEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    composable(HomeSections.FOOD.route) { from ->
        Food(onFoodSelected = { onSelected(it, from) }, modifier = modifier)
    }
    composable(HomeSections.RECIPE.route) { from ->
        Text(text = HomeSections.RECIPE.name)
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
    onFoodSelected: (Long) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(text = HomeSections.FOOD.name)
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
