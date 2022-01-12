package com.skifapp.skiflab.app

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalActivity = staticCompositionLocalOf<ComponentActivity> { error("NoActivityProvided") }

@Composable
fun MainActivity.ProvideActivityLocals(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalActivity provides this, content = content
    )
}