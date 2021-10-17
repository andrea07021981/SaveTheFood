package com.example.savethefood.ui.compose

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.savethefood.ui.theme.SaveTheFoodTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import kotlin.properties.Delegates

// TODO use composition local to pass down through the composition alpha and text color (codelab layout)
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
    ProvideWindowInsets {
        SaveTheFoodTheme {
            content()
        }
    }
}

@Composable
fun MainApp() {
    // A surface container using the 'color' color from the theme
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
                    // TODO MOVE IT INTO NAVGRAPH? WE CAN USE systemBarsPadding FOR BOTH TOP AND BOTTOM NAV
                    // TODO here https://medium.com/mobile-app-development-publication/android-jetpack-compose-inset-padding-made-easy-5f156a790979
                    modifier = Modifier.statusBarsPadding(),
                    contentColor = SaveTheFoodTheme.colors.textPrimary,
                    bottomBar = { MainBottomNav(navController = navController, tabs = tabs) },
                    scaffoldState = scaffoldState
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

}