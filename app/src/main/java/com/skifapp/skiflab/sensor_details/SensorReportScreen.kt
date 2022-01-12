package com.skifapp.skiflab.sensor_details

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.skifapp.skiflab.R
import com.skifapp.skiflab.models.ChartData
import com.skifapp.skiflab.util.orbit.collectAsState
import com.skifapp.skiflab.util.voyager.getParentViewModel
import me.sungbin.timelineview.TimeLine
import me.sungbin.timelineview.TimeLineItem
import org.joda.time.DateTime

data class TimeLineData(
    override val key: Int,
    val data: ChartData
) : TimeLineItem<Int>

class SensorReportScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel: SensorDetailsViewModel = getParentViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val state by viewModel.collectAsState()
        val data by remember(state.chardData) {
            derivedStateOf {
                state.chardData.filterNotNull().groupBy { DateTime(it.date).dayOfYear }
                    .map { entry ->
                        entry.value.takeLast(50).map { value ->
                            TimeLineData(entry.key, value)
                        }
                    }.flatten()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.report_title),
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                                contentDescription = null
                            )
                        }
                    },
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 8.dp
                )
            },
            modifier = Modifier
                .navigationBarsWithImePadding()
                .statusBarsPadding(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Показания по дачитку ${state.sensor?.name} за последний месяц",
                    style = MaterialTheme.typography.body1
                )
                Divider()


                TimeLine(items = data, header = { dayOfYear ->
                    val time = DateTime().withDayOfYear(dayOfYear)
                    Text(text = "$time", style = MaterialTheme.typography.subtitle2)
                }) { item ->
                    val isLess = remember(item, data) {
                        val prev = data.getOrNull(item.key - 1)
                        prev != null && prev.data.value > item.data.value
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "P = ${item.data.value}",
                            style = MaterialTheme.typography.body2
                        )
                        if (isLess) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_trending_down_24),
                                contentDescription = null,
                                tint = Color.Green
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_trending_up_24),
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    }
                }

            }
        }
    }
}