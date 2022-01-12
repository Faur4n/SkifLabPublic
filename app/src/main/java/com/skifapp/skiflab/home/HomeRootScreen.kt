package com.skifapp.skiflab.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

class HomeRootScreen: AndroidScreen() {

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    override fun Content() {

        Navigator(screen = HomeScreen()){ navigator ->
            SlideTransition(navigator)
        }
    }
}