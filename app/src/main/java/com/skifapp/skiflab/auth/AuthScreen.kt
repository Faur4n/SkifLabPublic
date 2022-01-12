package com.skifapp.skiflab.auth

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.skifapp.skiflab.R
import com.skifapp.skiflab.auth.enter_code.EnterCodeScreen
import com.skifapp.skiflab.auth.enter_phone.EnterPhoneScreen
import com.skifapp.skiflab.home.HomeRootScreen
import com.skifapp.skiflab.home.HomeScreen
import com.skifapp.skiflab.util.orbit.collectSideEffect

class AuthScreen : AndroidScreen() {

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    override fun Content() {
        val viewModel : AuthViewModel = getViewModel()

        Scaffold(modifier = Modifier
            .navigationBarsWithImePadding()
            .statusBarsPadding()) {

            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.height(64.dp)
                )
                Navigator(screen = EnterPhoneScreen()){ navigator ->
                    viewModel.collectSideEffect { effect ->
                        when (effect) {
                            AuthSideEffect.EnterCode -> {
                                navigator.push(EnterCodeScreen())
                            }
                            AuthSideEffect.LoggedIn -> {
                                navigator.parent?.replaceAll(HomeRootScreen())
                            }
                            AuthSideEffect.Cancel -> {
                                navigator.replaceAll(EnterPhoneScreen())
                            }
                        }
                    }
                    SlideTransition(navigator = navigator)
                }
            }
        }
    }
}