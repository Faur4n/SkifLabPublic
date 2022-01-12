package com.skifapp.skiflab.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.skifapp.skiflab.R
import com.skifapp.skiflab.auth.login.LoginScreen
import com.skifapp.skiflab.util.orbit.collectAsState
import com.skifapp.skiflab.util.orbit.collectSideEffect
import com.skifapp.skiflab.util.voyager.getParentViewModel

tailrec fun Navigator.findRootNavigator(): Navigator {
    val parent = parent
    return if (parent == null) {
        this
    } else {
        parent.findRootNavigator()
    }
}

class SettingsScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: HomeViewModel = getParentViewModel()
        val state by viewModel.collectAsState()
        var expanded by remember { mutableStateOf(false) }
        viewModel.collectSideEffect { effect ->
            when (effect) {
                HomeSideEffect.Error -> {}
                HomeSideEffect.LoggedOut -> {
                    navigator.findRootNavigator().replaceAll(LoginScreen())
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Настройки",
                        )
                    }, navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                                contentDescription = null
                            )
                        }
                    },
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 8.dp
                )
            }, modifier = Modifier
                .navigationBarsWithImePadding()
                .statusBarsPadding()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expanded = !expanded
                        }
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Компания", style = MaterialTheme.typography.caption)
                        Text(
                            text = state.currentAccess?.nameModule.toString(),
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_down_24),
                        contentDescription = null
                    )
                }
                Divider(modifier = Modifier.fillMaxWidth())
                AnimatedVisibility(visible = expanded) {
                    state.accesses.forEach { access ->
                        Column() {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                    }
                                    .padding(horizontal = 8.dp, vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = access.nameModule.toString(),
                                    style = MaterialTheme.typography.body1
                                )
                            }
                            Divider(modifier = Modifier.fillMaxWidth())
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .clickable {
                            viewModel.logout()
                        }
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                        .fillMaxWidth()

                ) {
                    Text(
                        text = "Выйти",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.error
                    )
                }
                Divider(modifier = Modifier.fillMaxWidth())

            }

        }
    }
}