package com.example.savethefood.ui.compose

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import androidx.compose.material.FabPosition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

@Composable
fun MainApp() {
    // A surface container using the 'color' color from the theme
    // TODO maybe remove and pass the colors for the Splash
    Surface(
        contentColor = SaveTheFoodTheme.colors.textPrimary,
        color = SaveTheFoodTheme.colors.uiBackground,
    ) {
        var showSplashScreen by remember {
            mutableStateOf(true)
        }

        // Show the splash screen first
        Crossfade(
            targetState = showSplashScreen,
            animationSpec = tween(durationMillis = 3000, easing = FastOutSlowInEasing)
        ) { isSplash ->
            if (isSplash) {
                SplashScreen(onTimeOut = { showSplashScreen = false })
            } else {
                // TODO Use custom state as state holders as source of truth https://developer.android.com/jetpack/compose/state#types-of-state-and-logic
                val tabs = remember { HomeSections.values() }
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                SaveTheFoodScaffold(
                    // This add the space of the status bar since have enabled setDecorFitsSystemWindows
                    // TODO now we use statusBarsPadding for top padding, MOVE IT INTO NAVGRAPH? WE CAN USE systemBarsPadding FOR BOTH TOP AND BOTTOM NAV
                    // TODO here https://medium.com/mobile-app-development-publication/android-jetpack-compose-inset-padding-made-easy-5f156a790979
                    modifier = Modifier.systemBarsPadding(),
                    contentColor = SaveTheFoodTheme.colors.textPrimary,
                    bottomBar = { MainBottomNav(navController = navController, tabs = tabs) },
                    floatingActionButton = {
                        FloatingActionButton(
                            shape = CircleShape,
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                        }
                    },
                    isFloatingActionButtonDocked = true,
                    floatingActionButtonPosition = FabPosition.Center,
                    scaffoldState = scaffoldState,
                ) { innerPaddingModifier ->
                    MainNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPaddingModifier)
                    )
                }
            }
        }
    }
}

@Preview
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewApp() {
    MainApp()
}