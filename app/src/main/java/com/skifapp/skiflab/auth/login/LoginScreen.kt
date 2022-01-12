package com.skifapp.skiflab.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.skifapp.skiflab.R
import com.skifapp.skiflab.auth.AuthScreen

class LoginScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(12.dp)
                .statusBarsPadding()
                .navigationBarsPadding()
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.height(64.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.launch_image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(text = "ЖКХ некогда не было проще", style = MaterialTheme.typography.h6)
                Text(
                    text = "Удобное меню для всех ващих счетчиков в один клик",
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = { navigator.replaceAll(AuthScreen()) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(12.dp)
            ) {
                Text(text = stringResource(id = R.string.login))
            }

        }
    }
}