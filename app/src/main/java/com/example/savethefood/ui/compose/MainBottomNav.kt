package com.example.savethefood.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.savethefood.ui.theme.SaveTheFoodTheme

// TODO create custom BottomAppBar for Cradle shade
@Composable
fun MainBottomNav(
    navController: NavController,
    tabs: Array<HomeSections>,
    color: Color = SaveTheFoodTheme.colors.uiBackground,
    contentColor: Color = SaveTheFoodTheme.colors.iconInteractive
) {
    // TODO Difference with BottomNavigation?? Is it correct from UI/UX side? Can we manage the add food fab differently?
    BottomAppBar(
        cutoutShape = CircleShape, // TODO change this for the shape? Create a custom bottom nav?
        backgroundColor = color,
        contentColor = contentColor
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val currentRoute = navBackStackEntry?.destination?.route

        //does not work, create a custom bottom like jet
        tabs.forEach { section ->
            BottomNavigationItem(
                icon = { Icon(section.icon, contentDescription = null) },
                label = { Text(stringResource(section.title)) },
                selected = currentDestination?.hierarchy?.any { it.route == section.route } == true,
                onClick = {
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
            )
        }
    }
}