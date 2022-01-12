package com.skifapp.skiflab.sensor_details

import androidx.lifecycle.ViewModel
import com.skifapp.skiflab.home.repository.HomeRepository
import com.skifapp.skiflab.models.ChartData
import com.skifapp.skiflab.models.Tsensor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class SensorDetailsState(
    val sensor: Tsensor? = null,
    val currentToggle: PeriodToggle = PeriodToggle.DAY,
    val chardData: List<ChartData?> = arrayOfNulls<ChartData>(12).toList()
)


@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class SensorDetailsViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel(), ContainerHost<SensorDetailsState, Nothing> {

    override val container: Container<SensorDetailsState, Nothing> = container(SensorDetailsState())

    fun init(id: String) {
        intent {
            repository.getSensor(id).collect {
                reduce {
                    state.copy(sensor = it)
                }
            }
        }
        intent {
            container.stateFlow.map { it.currentToggle }.flatMapLatest { toggle ->
                repository.getChardData(id, toggle)
            }.collect { data ->
                reduce {
                    state.copy(chardData = state.chardData
                        .toMutableSet()
                        .apply { addAll(data) }
                        .asSequence()
                        .filterNotNull()
                        .sortedByDescending { it.date }
                        .toList()
                    )
                }
            }
        }
    }

    fun updateToggle(toggle: PeriodToggle) = intent {
        reduce {
            state.copy(currentToggle = toggle)
        }
    }
}