package com.example.savethefood.ui.compose

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.savethefood.ui.compose.extention.isSectionSelected
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding

// TODO use composition local to pass down through the composition alpha and text color (codelab layout)
// TODO use inset ime for the keyboard in home and recipe while using the filter and the keyboard https://medium.com/mobile-app-development-publication/android-jetpack-compose-inset-padding-made-easy-5f156a790979

/**
 * CompositionLocalProvider(
LocalContentAlpha provides ContentAlpha.medium,
LocalContentColor provides Purple700 ) {
Text("3 minutes ago", style = MaterialTheme.typography.body2)
}
 */
// TODO (advancedStateCodelab) use managing states with state holders for the add/edit food to change the styles/effects of the ui
// TODO also use JetpackComposeLayout for vm and adding food UI
@Composable
fun SaveTheFoodApp(content: @Composable () -> Unit) {
    // TODO READ INSETS https://medium.com/mobile-app-development-publication/android-jetpack-compose-inset-padding-made-easy-5f156a790979 and https://google.github.io/accompanist/insets/
    SaveTheFoodTheme {
        ProvideWindowInsets {
            content()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainApp() {
    // A surface container using the 'color' color from the theme
    Surface(
        contentColor = SaveTheFoodTheme.colors.textPrimary,
        color = SaveTheFoodTheme.colors.uiBackground,
    ) {
        // DONE Use custom state as state holders as source of truth https://developer.android.com/jetpack/compose/state#types-of-state-and-logic
        //val tabs = remember { HomeSections.values() }
        val appState = rememberAppState()
        //val navController = rememberNavController()
        // Manage the visibility of bottom nav
        //val currentBackStackEntry by navController.currentBackStackEntryAsState()
        //val scaffoldState = rememberScaffoldState()
        SaveTheFoodScaffold(
            // This add the space of the status bar since have enabled setDecorFitsSystemWindows
            // TODO now we use statusBarsPadding for top padding, MOVE IT INTO NAVGRAPH? WE CAN USE systemBarsPadding FOR BOTH TOP AND BOTTOM NAV
            // TODO here https://medium.com/mobile-app-development-publication/android-jetpack-compose-inset-padding-made-easy-5f156a790979
            modifier = Modifier.systemBarsPadding(),
            contentColor = SaveTheFoodTheme.colors.textPrimary,
            bottomBar = {
                if (appState.hasBottomNav) {
                    MainBottomNav(
                        //navController = appState.navController,
                        tabs = appState.tabs,
                        navBackStackEntry = appState.navBackStackEntry,
                        selected = { dest, section ->
                            dest?.isSectionSelected(section) ?: false
                        },
                        navigateTo = { section, currentRoute ->
                            appState.navigateToDestination(section, currentRoute)
                        }
                    )
                }
            },
            scaffoldState = appState.scaffoldState,
        ) { innerPaddingModifier ->
            // TODO add topbar common here? Otherwise every screen that needs it add scaffold like Jetnews
            MainNavGraph(
                navController = appState.navController,
                modifier = Modifier
                    .padding(innerPaddingModifier)
                    .fillMaxSize()
            )
        }
    }
}

@Preview
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewApp() {
    MainApp()
}