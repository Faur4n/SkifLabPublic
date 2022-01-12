package com.skifapp.skiflab.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import cafe.adriel.voyager.transitions.SlideTransition
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.skifapp.skiflab.home.HomeScreen
import com.skifapp.skiflab.auth.login.LoginScreen
import com.skifapp.skiflab.home.HomeRootScreen
import com.skifapp.skiflab.ui.theme.SkifLabTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepVisibleCondition{
            viewModel.isLoading
        }

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }

            SkifLabTheme {
                ProvideActivityLocals{
                    ProvideWindowInsets {
                        val startScreen = if(viewModel.checkIsAuthorized()){
                            HomeRootScreen()
                        }else{
                            LoginScreen()
                        }
                        Navigator(screen = startScreen){ navigator ->
                            SlideTransition(navigator = navigator)
                        }
                    }
                }
            }
        }

    }

}


