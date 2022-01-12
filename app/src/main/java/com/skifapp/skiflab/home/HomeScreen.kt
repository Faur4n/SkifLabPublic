package com.skifapp.skiflab.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.skifapp.skiflab.R
import com.skifapp.skiflab.sensor_details.SensorDetailsScreen
import com.skifapp.skiflab.ui.components.SensorCard
import com.skifapp.skiflab.util.orbit.collectAsState
import com.skifapp.skiflab.util.voyager.getParentViewModel

class HomeScreen : AndroidScreen() {

    @OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val viewModel: HomeViewModel = getParentViewModel()
        val state by viewModel.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val keyboard = LocalSoftwareKeyboardController.current

        LifecycleEffect(onStarted = {
            keyboard?.hide()
        })

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = state.currentAccess?.nameModule.toString(),
                            modifier = Modifier.placeholder(
                                state.currentAccess == null,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            navigator.push(SettingsScreen())
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_settings_24),
                                contentDescription = null
                            )
                        }
                    },
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 8.dp
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navigator.push(AddSensorDataScreen()) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier
                .navigationBarsWithImePadding()
                .statusBarsPadding(),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.sensors) { sensor ->
                    SensorCard(
                        sensor,
                    ) {
                        sensor?.id?.let { id ->
                            navigator.push(SensorDetailsScreen(id))
                        }
                    }
                }
            }
        }
    }
}