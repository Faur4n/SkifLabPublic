package com.skifapp.skiflab.sensor_details

import android.graphics.Color
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.skifapp.skiflab.R
import com.skifapp.skiflab.models.ChartData
import com.skifapp.skiflab.models.Tsensor
import com.skifapp.skiflab.ui.components.SensorCard
import com.skifapp.skiflab.util.orbit.collectAsState
import com.skifapp.skiflab.util.voyager.getParentViewModel
import java.text.SimpleDateFormat
import java.util.*


class SensorDetailsContentScreen : AndroidScreen() {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val viewModel: SensorDetailsViewModel = getParentViewModel()
        val state by viewModel.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = state.sensor?.name.toString(),
                            modifier = Modifier.placeholder(
                                state.sensor == null,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                        )
                    },
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 8.dp,
                    navigationIcon = {
                        IconButton(onClick = { navigator.popUntilRoot() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { navigator.push(SensorReportScreen()) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_query_stats_24),
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            modifier = Modifier
                .navigationBarsWithImePadding()
                .statusBarsPadding(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                SensorCard(
                    sensor = state.sensor,
                    elevation = 2.dp,
                    modifier = Modifier.padding(8.dp)
                )
                Card(modifier = Modifier.padding(8.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "График", style = MaterialTheme.typography.caption)
                        LineChart(
                            data = state.chardData.take(50).filterNotNull(),
                            sensor = state.sensor,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }
                Card(modifier = Modifier.padding(8.dp)) {
                    Column() {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.surface),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            PeriodToggle.values().forEach { toggle ->
                                OutlinedButton(
                                    modifier = Modifier
                                        .toggleable(
                                            value = toggle == state.currentToggle,
                                            role = Role.RadioButton,
                                            onValueChange = {
                                                viewModel.updateToggle(toggle)
                                            },
                                        )
                                        .weight(1f), onClick = {
                                        viewModel.updateToggle(toggle)
                                    }, colors = ButtonDefaults.outlinedButtonColors(
                                        backgroundColor = if (toggle == state.currentToggle)
                                            MaterialTheme.colors.primary
                                        else
                                            MaterialTheme.colors.surface,
                                        contentColor = if (toggle == state.currentToggle)
                                            MaterialTheme.colors.onPrimary
                                        else
                                            MaterialTheme.colors.onSurface
                                    )
                                ) {
                                    Text(text = stringResource(id = toggle.title))
                                }
                            }
                        }
                        state.chardData.take(3).forEach { data ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .placeholder(
                                        data == null,
                                        highlight = PlaceholderHighlight.shimmer()
                                    )
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = data?.date.toString(),
                                        style = MaterialTheme.typography.subtitle1
                                    )
                                    Text(
                                        text = data?.value.toString(),
                                        style = MaterialTheme.typography.caption
                                    )
                                }
                            }
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp), horizontalArrangement = Arrangement.End
                        ) {
                            val rowsCount = remember(state.chardData) {
                                state.chardData.size - 3
                            }
                            if (rowsCount > 3) {
                                Text(
                                    text = "Еще $rowsCount показаний...",
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            OutlinedButton(onClick = { /*TODO*/ }) {
                                Text(text = stringResource(id = R.string.show_more))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LineChart(
    data: List<ChartData>,
    sensor: Tsensor?,
    modifier: Modifier = Modifier
) {
    AndroidView(factory = { context ->
        com.github.mikephil.charting.charts.LineChart(context).apply {
            setBackgroundColor(Color.WHITE)
            description.isEnabled = false
            setTouchEnabled(true)
            setDrawGridBackground(false)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)

            axisRight.isEnabled = false
            xAxis.valueFormatter = DatepointValueFormatter()
            xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE

        }
    }, modifier = modifier) { chart ->
        val entries = data.map {
            Entry(it.date.time.toFloat(), it.value.toFloat())
        }
        val dataSet = LineDataSet(entries, sensor?.name.toString())
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawValues(false)
        dataSet.lineWidth = 3.5f
        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()
        chart.animateX(500)
    }
}

class DatepointValueFormatter : ValueFormatter() {

    private val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val date = Date(value.toLong())
        return formatter.format(date)
    }
}