package com.skifapp.skiflab.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    val isLoading : Boolean
        get() = _isLoading.value

    fun checkIsAuthorized(): Boolean {
        return (Firebase.auth.currentUser != null).also {
            viewModelScope.launch {
                _isLoading.emit(false)
            }
        }
    }

}