package com.skifapp.skiflab.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.skifapp.skiflab.R


@Composable
fun SkifTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: String? = null,
    label: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
){
    var isFocused by remember { mutableStateOf(false) }
    Column() {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(12.dp),
            label = label,
            modifier = modifier.onFocusChanged {
                isFocused = it.isFocused
            },
            isError = error != null,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            enabled = enabled,
            trailingIcon = {
                if(isFocused && value.isNotBlank()){
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_clear_24), contentDescription = null)
                    }
                }
            }
        )
        if(error != null){
            Text(text = error,style = MaterialTheme.typography.caption, color = MaterialTheme.colors.error)
        }
    }
}