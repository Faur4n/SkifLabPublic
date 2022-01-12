package com.skifapp.skiflab.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import logcat.logcat
import me.ibrahimsn.lib.PhoneNumberKit
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class AuthState(
    val phone: String = "",
    val isValidPhone: Boolean = false,
    val code: String = "",
    val isValidCode: Boolean = false,
    val error: Exception? = null,
    val isLoading : Boolean = false
)

sealed class AuthSideEffect {
    object EnterCode : AuthSideEffect()

    object LoggedIn : AuthSideEffect()

    object Cancel: AuthSideEffect()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val phoneNumberKit: PhoneNumberKit
) : ViewModel(), ContainerHost<AuthState, AuthSideEffect> {

    var storedVerificationId: String? = null
    var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    override val container: Container<AuthState, AuthSideEffect> = container(AuthState())

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            logcat { "onVerificationCompleted:$credential" }
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            logcat { "onVerificationFailed ${e.stackTraceToString()}" }

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {

            logcat { "onCodeSent:$verificationId" }
            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
            intent {
                postSideEffect(AuthSideEffect.EnterCode)
                reduce {
                    state.copy(isLoading = false)
                }
            }
        }
    }

    fun updatePhone(value: String) = intent {
        val parsedNumber = phoneNumberKit.parsePhoneNumber(
            number = value,
            defaultRegion = "ru"
        )

        reduce {
            state.copy(phone = value, isValidPhone = parsedNumber != null)
        }
    }

    fun updateCode(value: String) = intent {
        val isValid = value.length == 6
        reduce {
            state.copy(code = value, isValidCode = isValid)
        }
    }

    fun submitPhone(activity: Activity) = intent {
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(state.phone)
            .setActivity(activity)// Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        reduce {
            state.copy(isLoading = true)
        }
    }

    fun submitCode() = intent{
        reduce {
            state.copy(isLoading = true)
        }
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, state.code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) = intent{
        val result = Firebase.auth.signInWithCredential(credential)
            .await()
        if(result != null && result.user != null){
            postSideEffect(AuthSideEffect.LoggedIn)
        }

        reduce {
            state.copy(isLoading = false)
        }
    }

    fun cancelVerification(){
        intent{
            postSideEffect(AuthSideEffect.Cancel)

            reduce {
                state.copy(isLoading = false)
            }
        }
    }

}