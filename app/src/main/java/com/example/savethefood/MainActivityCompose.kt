package com.example.savethefood

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.savethefood.ui.compose.MainApp
import com.example.savethefood.ui.compose.SaveTheFoodApp
import com.example.savethefood.ui.theme.SaveTheFoodTheme

// TODO use same structure with LazyRow for home page like Jeetpack feed OR
// TODO  follow todo app here (vertical list for storage type (5 elements by due date closer), click + to add, click arrow to eexpand the list ) https://www.justinmindH.com/blog/list-ui-design/
// TODO OR horizontal view with 5 items close to best before, click arrow and list switch to veertical and expands
class MainActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        // Animate the exit
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            // Create your custom animation.
            ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.ALPHA,
                1f,
                0F
            ).also {
                it.interpolator = AnticipateInterpolator()
                it.duration = 2000L

                // Call SplashScreenView.remove at the end of your custom animation.
                it.doOnEnd { splashScreenView.remove() }

                // Run your animation.
                it.start()
            }
        }

        // DONE Enable it when home scaffold is ready
        // This app draws behind the system bars, so we want to handle fitting system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SaveTheFoodApp {
                MainApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SaveTheFoodApp {
        MainApp()
    }
}