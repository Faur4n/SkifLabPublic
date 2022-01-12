package com.skifapp.skiflab.data

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.skifapp.skiflab.models.Access
import com.skifapp.skiflab.models.ChartData
import com.skifapp.skiflab.models.Geometry
import com.skifapp.skiflab.models.Tsensor
import com.skifapp.skiflab.sensor_details.PeriodToggle
import com.skifapp.skiflab.sensor_details.limit
import com.skifapp.skiflab.sensor_details.startValue
import com.skifapp.skiflab.util.parseDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import logcat.logcat
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FirebaseDataSource @Inject constructor() {

    private val db = Firebase.firestore
    private val counterRef = Firebase.database.reference.child("counter")

    fun myAccesses(uid: String): Flow<List<Access>> = callbackFlow {
        val listener = db.collection("access")
            .whereEqualTo("gkey", uid)
            .addSnapshotListener { col, error ->
                val access = if (col != null && !col.isEmpty) {
                    col.toObjects<Access>()
                } else emptyList()

                trySend(access)
            }

        awaitClose { listener.remove() }
    }

    fun tsensors(idModule: String, geometries: List<String>): Flow<List<Tsensor>> = callbackFlow {
        val listener = db.collection("modules/${idModule}/tsensors")
            .whereIn("id_geometry", geometries)
            .addSnapshotListener { col, error ->
                val tsensors = if (col != null && !col.isEmpty) {
                    col.toObjects<Tsensor>()
                } else emptyList()
                trySend(tsensors)
            }

        awaitClose { listener.remove() }
    }


    fun geometrys(idCompany: String, idFlat: String): Flow<List<Geometry>> = callbackFlow {
        val listener = db.collection("companys/$idCompany/geometrys")
            .whereEqualTo("id_flat", idFlat)
            .addSnapshotListener { col, error ->
                val geometry = if (col != null && !col.isEmpty) {
                    col.toObjects<Geometry>()
                } else emptyList()
                trySend(geometry)
            }

        awaitClose { listener.remove() }
    }

    inline fun <reified T> List<Flow<T>>.flattenFlow(): Flow<List<T>> = combine(this@flattenFlow) {
        it.toList()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun withCounterValues(flow: Flow<List<Tsensor>>): Flow<List<Tsensor>> {
        return flow.flatMapLatest { list ->
            list.map { tsensor ->
                callbackFlow {
                    val ref = counterRef.child(tsensor.id.toString()).limitToLast(1)
                    trySend(tsensor)
                    val listener = object : ChildEventListener {
                        override fun onChildAdded(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                            val value = snapshot.child("value").value.toString().toLongOrNull()
                            val datepoint = snapshot.child("datepoint").value.toString()
                            trySend(tsensor.copy(value = value, datepoint = datepoint))
                        }

                        override fun onChildChanged(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                        }

                        override fun onChildRemoved(snapshot: DataSnapshot) {}
                        override fun onChildMoved(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    }
                    ref.addChildEventListener(listener)
                    awaitClose { ref.removeEventListener(listener) }
                }
            }.flattenFlow()
        }
    }

    fun getChardData(idTsensor: String, period: PeriodToggle): Flow<List<ChartData>> {
        logcat { "getChardData $idTsensor" }
        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.hasChildren()){
                        val data = snapshot.children.mapNotNull { ch ->
                            ch.toChartData()
                        }
                        trySend(data)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }

            val ref = counterRef.child(idTsensor)
                .orderByChild(period.optionName)
//                .limitToFirst(25)
//                .startAt(period.startValue)
                .limitToFirst(period.limit)

            val refValues = ref.get().await()
            if(refValues.hasChildren()){
                val data = refValues.children.mapNotNull { ch ->
                    ch.toChartData()
                }
                trySend(data)
                ref.addListenerForSingleValueEvent(listener)
            }else trySend(emptyList())

            logcat { "rows ${refValues.childrenCount}" }

            awaitClose { ref.removeEventListener(listener) }
        }
    }

    private fun DataSnapshot.toChartData(): ChartData? {
        val datepoint = child("datepoint").value.toString()
        val time = datepoint.parseDate() ?: return null
        val value = child("value").value.toString().toLongOrNull() ?: return null
        return ChartData(time, value)
    }

}