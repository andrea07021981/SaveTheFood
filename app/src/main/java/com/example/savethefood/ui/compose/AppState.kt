package com.example.savethefood.ui.compose

import android.content.res.Resources
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.savethefood.ui.compose.extention.isHomeSection
import com.example.savethefood.ui.compose.navigation.Screen

/**
 * Define the state of the main app with the main components
 */
@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    resources: Resources = LocalContext.current.resources,
) = remember(scaffoldState, navController, resources) {
    AppState(scaffoldState, navController, resources)
}

/**
 * App state holder
 */

class AppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val resources: Resources
) {
    val tabs by mutableStateOf(
        Screen.Home::class.sealedSubclasses.map { it.objectInstance }.toTypedArray()
    )

    private val currentBackStackEntry: NavBackStackEntry?
        @Composable get() = navController.currentBackStackEntryAsState().value

    val navBackStackEntry: NavBackStackEntry?
        @Composable get() = navController.currentBackStackEntryAsState().value

    val hasBottomNav: Boolean
        @Composable get() {
            return currentBackStackEntry?.destination?.route?.isHomeSection == true
        }

    /**
     * Navigate to a specific destination
     */
    fun navigateToDestination(section: Screen.Home, currentRoute: String?) {
        // Check avoid reload same page
        if (section.route != currentRoute) {
            navController.navigate(section.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    }
}