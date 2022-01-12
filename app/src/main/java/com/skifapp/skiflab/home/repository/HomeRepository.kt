package com.skifapp.skiflab.home.repository

import com.skifapp.skiflab.models.AppState
import com.skifapp.skiflab.models.ChartData
import com.skifapp.skiflab.models.Tsensor
import com.skifapp.skiflab.sensor_details.PeriodToggle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface HomeRepository {

    val appState: StateFlow<AppState?>

    fun getSensor(id: String): Flow<Tsensor>

    fun getChardData(id: String,periodToggle: PeriodToggle) : Flow<List<ChartData>>

}