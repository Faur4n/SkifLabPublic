package com.skifapp.skiflab.home.repository.impl

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.ktx.Firebase
import com.skifapp.skiflab.data.FirebaseDataSource
import com.skifapp.skiflab.data.PreferenceManager
import com.skifapp.skiflab.home.repository.HomeRepository
import com.skifapp.skiflab.models.AppState
import com.skifapp.skiflab.models.ChartData
import com.skifapp.skiflab.models.Tsensor
import com.skifapp.skiflab.sensor_details.PeriodToggle
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import logcat.logcat
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import java.lang.Exception
import java.lang.NullPointerException
import javax.inject.Inject


sealed class LoadStatus<T> {

    data class Error<T>(val message: String, val e: Exception) : LoadStatus<T>()

    data class Success<T>(val data: T) : LoadStatus<T>()
}



@ActivityRetainedScoped
@OptIn(ExperimentalCoroutinesApi::class)
class HomeRepositoryImpl @Inject constructor(
    private val firebase: FirebaseDataSource,
    private val preferenceManager: PreferenceManager,
    scope: CoroutineScope,
) : HomeRepository {

    @InstallIn(ActivityRetainedComponent::class)
    @Module
    interface HomeRepositoryModule {

        @Binds
        fun bind(impl: HomeRepositoryImpl): HomeRepository
    }

    override fun getSensor(id: String): Flow<Tsensor> {
        return appState.mapNotNull { it?.sensors?.firstOrNull { sensor -> sensor?.id == id } }
    }

    override fun getChardData(id: String,periodToggle: PeriodToggle): Flow<List<ChartData>> {
        return firebase.getChardData(id, period = periodToggle)
    }

    override val appState = getAppStateFlow()
        .stateIn(scope, SharingStarted.Lazily, AppState())

    private fun getAppStateFlow(): Flow<AppState?> {
        val uid = Firebase.auth.uid
        if (uid == null) {
            logcat { "uid is null" }
            return flowOf(null)
        }
        return combine(
            firebase.myAccesses(uid).map { list ->
                list.filter { it.typeModule == "hmg" }
            },
            preferenceManager.get(PreferenceManager.currentAccessKey)
        ) { accesses, currentId ->
            logcat { "accesses is $accesses" }
            logcat { "current is $currentId" }

            val access = if (currentId == null) {
                accesses.firstOrNull()
            } else accesses.find { it.id == currentId }

            AppState(
                accesses = accesses,
                currentAccess = access
            )
        }.flatMapLatest { state ->
            val tsensors = state.currentAccess?.let { access ->
                val currentModule = access.idModule.toString()
                val idFlat = access.idFlat.toString()
                val idCompany = access.idCompany.toString()

                firebase.geometrys(idCompany, idFlat).flatMapLatest { geometry ->
                    logcat { "geometry is $geometry" }
                    firebase.withCounterValues(
                        combine(
                            *geometry.map { it.id.toString() }.chunked(10).map {
                                firebase.tsensors(currentModule, it)
                            }.toTypedArray()
                        ) { arr ->
                            arr.toList().flatten()
                        }
                    )
                }
            } ?: flowOf(emptyList())

            tsensors.map { list ->
                state.copy(sensors = list)
            }
        }
    }



}