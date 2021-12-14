package com.example.savethefood.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.savethefood.ui.compose.navigation.Screen
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import kotlin.reflect.KClass

// TODO create custom BottomAppBar for Cradle shade
@Composable
fun MainBottomNav(
    //navController: NavController,
    tabs: Array<Screen.Home?>,
    navBackStackEntry: NavBackStackEntry?,
    selected: (NavDestination?, Screen.Home) -> Boolean,
    color: Color = SaveTheFoodTheme.colors.uiBackground,
    contentColor: Color = SaveTheFoodTheme.colors.iconInteractive,
    navigateTo: (Screen.Home, String?) -> Unit
) {
    // TODO Difference with BottomNavigation?? Is it correct from UI/UX side? Can we manage the add food fab differently?
    BottomAppBar(
        backgroundColor = color,
        contentColor = contentColor
    ) {
        //val navBackStackEntry by navController.currentBackStackEntryAsState()
        /*val currentDestination = navBackStackEntry?.destination
        val currentRoute = currentDestination?.route*/

        //does not work as expected, create a custom bottom like jet
        tabs.forEach { tab ->
            tab?.let { section ->
                BottomNavigationItem(
                    icon = { Icon(section.icon, contentDescription = null) },
                    label = { Text(stringResource(section.title)) },
                    selected = selected(navBackStackEntry?.destination, section),
                    onClick = {
                        navigateTo(section, navBackStackEntry?.destination?.route)
                        /*// Check avoid reload same page
                        if (section.route != currentRoute) {
                            navigateTo(section, currentRoute)
                            *//*navController.navigate(section.route) {
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
                        }*//*
                    }*/
                    }
                )
            }
        }
    }
}