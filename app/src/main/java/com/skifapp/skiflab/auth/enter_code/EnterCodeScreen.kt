package com.skifapp.skiflab.auth.enter_code

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.skifapp.skiflab.R
import com.skifapp.skiflab.auth.AuthViewModel
import com.skifapp.skiflab.ui.components.SkifTextField
import com.skifapp.skiflab.util.orbit.collectAsState
import com.skifapp.skiflab.util.voyager.getParentViewModel

class EnterCodeScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: AuthViewModel = getParentViewModel()
        val scrollState = rememberScrollState()
        val state by viewModel.collectAsState()
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
                    text = stringResource(id = R.string.enter_code_title),
                    style = MaterialTheme.typography.h4,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(id = R.string.enter_code_desc, state.phone),
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(16.dp))
                SkifTextField(
                    value = state.code,
                    onValueChange = viewModel::updateCode,
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text(stringResource(id = R.string.code_label))

                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                if (isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }


            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    onClick = { viewModel.submitCode() },
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Text(text = stringResource(id = R.string.next))
                }
                OutlinedButton(
                    onClick = { viewModel.cancelVerification() },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }

        }
    }
}