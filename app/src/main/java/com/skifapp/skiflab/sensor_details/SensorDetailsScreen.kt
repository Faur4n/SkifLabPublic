package com.skifapp.skiflab.sensor_details

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.skifapp.skiflab.R
import com.skifapp.skiflab.ui.components.SensorCard
import com.skifapp.skiflab.util.orbit.collectAsState
import org.joda.time.DateTime

enum class PeriodToggle(@StringRes val title: Int, val optionName: String) {
    DAY(R.string.day_title, "d"),

    //    WEEKS(R.string.week_title, "w"),
    MONTH(R.string.month_title, "m")
}

val PeriodToggle.startValue: Double
    get() {
        val time = DateTime().minusYears(1)
        val value = when (this) {
            PeriodToggle.DAY -> {
                val firstDay = time.withDayOfYear(1).dayOfYear - 1
                time.year * 1000 + firstDay
            }
            PeriodToggle.MONTH -> {
                val localDate = time.minusYears(1).withMonthOfYear(12)
                val firstMonth = localDate.monthOfYear
                val localYear = localDate.year % 100
                (localYear) * 1000 + firstMonth
            }
//            PeriodToggle.WEEKS -> {
//                (time.withWeekOfWeekyear(1).weekOfWeekyear - 1) * 1000
//            }
        }
        return value.toDouble()
    }


val PeriodToggle.limit: Int
    get() {
        return when (this) {
            PeriodToggle.DAY -> 365
            PeriodToggle.MONTH -> 13
//            PeriodToggle.WEEKS -> 5
        }
    }


class SensorDetailsScreen(
    private val id: String
) : AndroidScreen() {

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    override fun Content() {
        val viewModel: SensorDetailsViewModel = getViewModel()

        LifecycleEffect(onStarted = {
            viewModel.init(id)
        })

        Navigator(screen = SensorDetailsContentScreen()){ navigator ->
            SlideTransition(navigator)
        }

    }
}