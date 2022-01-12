package com.skifapp.skiflab.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.skifapp.skiflab.home.repository.HomeRepository
import com.skifapp.skiflab.models.Access
import com.skifapp.skiflab.models.Tsensor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


data class HomeState(
    val accesses: List<Access> = emptyList(),
    val currentAccess: Access? = null,
    val isLoading: Boolean = false,
    val sensors: List<Tsensor?> = arrayOfNulls<Tsensor>(5).toList()
)

sealed class HomeSideEffect {
    object Error : HomeSideEffect()

    object LoggedOut : HomeSideEffect()

    object NewValueSaved: HomeSideEffect()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel(), ContainerHost<HomeState, HomeSideEffect> {

    val auth = Firebase.auth

    override val container: Container<HomeState, HomeSideEffect> =
        container(HomeState(), onCreate = {
            intent {
                repository.appState.collect { newState ->
                    if (newState == null) {
                        postSideEffect(HomeSideEffect.Error)
                        return@collect
                    }
                    reduce {
                        state.copy(
                            accesses = newState.accesses,
                            currentAccess = newState.currentAccess,
                            sensors = newState.sensors
                        )
                    }
                }
            }
        })


    fun logout() = intent {
        auth.signOut()
        postSideEffect(HomeSideEffect.LoggedOut)
    }

    fun saveNewSensor(sensor: Tsensor,value: Int) = intent{
        reduce {
            state.copy(isLoading = true)
        }
        delay(2000)
        reduce {
            state.copy(isLoading = false)
        }
        postSideEffect(HomeSideEffect.NewValueSaved)
    }
}