package com.skifapp.skiflab.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.skifapp.skiflab.R
import com.skifapp.skiflab.models.Tsensor
import com.skifapp.skiflab.util.orbit.collectAsState
import com.skifapp.skiflab.util.orbit.collectSideEffect
import com.skifapp.skiflab.util.voyager.getParentViewModel

class AddSensorDataScreen : AndroidScreen() {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val viewModel: HomeViewModel = getParentViewModel()
        val state by viewModel.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        var showDropdown by remember { mutableStateOf(false) }
        var currentSensor by remember { mutableStateOf<Tsensor?>(null) }
        var newValue by remember { mutableStateOf("") }

        val saveEnabled = remember(newValue, currentSensor,state.isLoading) {
            newValue.isNotBlank() && currentSensor != null && !state.isLoading
        }

        viewModel.collectSideEffect { effect ->
            when (effect) {
                HomeSideEffect.Error -> {}
                HomeSideEffect.LoggedOut -> {}
                HomeSideEffect.NewValueSaved -> {
                    navigator.popUntilRoot()
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Добавить показания",
                        )
                    },
                    navigationIcon = {
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
            },
            modifier = Modifier
                .navigationBarsWithImePadding()
                .statusBarsPadding(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .fillMaxSize()
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Выбери счетчик по которому хотите добавить показания",
                        softWrap = true
                    )


                    ExposedDropdownMenuBox(
                        expanded = showDropdown,
                        onExpandedChange = { showDropdown = !showDropdown }) {

                        OutlinedTextField(
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            value = currentSensor?.name ?: "Выберите датчик",
                            onValueChange = { },
                            label = { Text("Label") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = showDropdown
                                )
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )

                        ExposedDropdownMenu(
                            expanded = showDropdown,
                            modifier = Modifier.fillMaxWidth(),
                            onDismissRequest = { showDropdown = false }) {
                            state.sensors.forEach { sensor ->
                                DropdownMenuItem(
                                    onClick = {
                                        currentSensor = sensor
                                        showDropdown = false
                                    }, modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = sensor?.name.toString())
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = newValue,
                        label = {
                            Text(text = "Показания")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { newValue = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Button(
                    onClick = {
                        currentSensor?.let { sensor ->
                            newValue.toIntOrNull()?.let { value ->
                                viewModel.saveNewSensor(sensor, value)
                            }
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    enabled = saveEnabled,
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 2.dp)
                    } else {
                        Text(text = stringResource(id = R.string.save))
                    }
                }

            }
        }
    }
}