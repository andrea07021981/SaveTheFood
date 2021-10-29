package com.example.savethefood.ui.compose

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.savethefood.shared.data.domain.FoodDomain
import com.example.savethefood.shared.utils.FoodImage
import com.example.savethefood.shared.utils.StorageType
import com.example.savethefood.ui.compose.pantry.FoodItem
import com.example.savethefood.ui.theme.SaveTheFoodTheme

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
            // TODO we need a generic since th onSeleected could be fooddomain, recipeDomain, etc.. We ecan make onSelected generic and use when is
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

/**
 * Add only the composable root navigation for the bottom nav
 */
fun NavGraphBuilder.addHomeGraph(
    onSelected: (Long, NavBackStackEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    composable(HomeSections.FOOD.route) { from ->
        // TODO test purpose, remove it
        // TODO add vm here and in every composable
        Food(
            onFoodSelected = { onSelected(it, from) },
            modifier = modifier
        ) {
            Text(text = HomeSections.FOOD.name)
        }
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
        // TODO TEst for list, I will remove it
        val data by rememberSaveable {
            mutableStateOf(
                listOf(
                    FoodDomain(
                        title = "Test 1",
                        img = FoodImage.ASPARAGUS,
                        storageType = StorageType.FRIDGE,
                        bestBefore = 1635542008818
                    ),
                    FoodDomain(
                        title = "Test 2",
                        img = FoodImage.BANANA,
                        storageType = StorageType.DRY,
                        bestBefore = 1634788800000
                    ),
                    FoodDomain(
                        title = "Test 3",
                        img = FoodImage.BEER,
                        storageType = StorageType.FREEZER,
                        bestBefore = 1637470800000
                    ),
                    FoodDomain(
                        title = "Test 4",
                        img = FoodImage.HAMBURGER,
                        storageType = StorageType.FREEZER,
                        bestBefore = 1632196800000
                    )
                )
            )
        }

        LazyColumn(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth())
        {
            items(data) {
                Column(modifier = Modifier.fillParentMaxWidth()) {
                    FoodItem(
                        foodDomain = it,
                        onFoodClick = {
                            Log.d("TEST", it.toString())
                        })
                }
            }
        }
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

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
