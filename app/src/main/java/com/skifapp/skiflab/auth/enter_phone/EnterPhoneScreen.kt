package com.skifapp.skiflab.auth.enter_phone

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.skifapp.skiflab.R
import com.skifapp.skiflab.app.LocalActivity
import com.skifapp.skiflab.auth.AuthSideEffect
import com.skifapp.skiflab.auth.AuthViewModel
import com.skifapp.skiflab.auth.enter_code.EnterCodeScreen
import com.skifapp.skiflab.home.HomeScreen
import com.skifapp.skiflab.ui.components.SkifTextField
import com.skifapp.skiflab.util.orbit.collectAsState
import com.skifapp.skiflab.util.orbit.collectSideEffect
import com.skifapp.skiflab.util.voyager.getParentViewModel

class EnterPhoneScreen : AndroidScreen() {

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val viewModel: AuthViewModel = getParentViewModel()
        val state by viewModel.collectAsState()
        val phoneRef = remember { FocusRequester() }
        val scrollState = rememberScrollState()
        val activity = LocalActivity.current
        val isLoading = remember(state) { state.isLoading }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = stringResource(id = R.string.auth_phone_title),
                    style = MaterialTheme.typography.h4,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(id = R.string.auth_phone_desc),
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(16.dp))
                SkifTextField(
                    value = state.phone,
                    onValueChange = viewModel::updatePhone,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    enabled = !isLoading,
                    label = {
                        Text(stringResource(id = R.string.phone_number_label))

                    },
                    modifier = Modifier
                        .focusRequester(phoneRef)
                        .fillMaxWidth(),
                )
                if (isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }

            Button(
                onClick = {
                    viewModel.submitPhone(activity)
                },
                shape = RoundedCornerShape(12.dp),
                enabled = state.isValidPhone && !isLoading,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(12.dp)
            ) {
                Text(text = stringResource(id = R.string.next))
            }
        }
    }
}