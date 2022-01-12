package com.skifapp.skiflab.util.voyager

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

@Composable
public inline fun <reified T : ViewModel> Screen.getParentViewModel(
): T {
    val screen =
        LocalNavigator.currentOrThrow.parent?.lastItemOrNull ?: error("No parent screen found")
    return screen.getViewModel()
}