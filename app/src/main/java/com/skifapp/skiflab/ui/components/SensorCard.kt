package com.skifapp.skiflab.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.skifapp.skiflab.R
import com.skifapp.skiflab.models.Tsensor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SensorCard(
    sensor: Tsensor?,
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .placeholder(
                sensor == null,
                highlight = PlaceholderHighlight.shimmer()
            ),
        elevation = elevation,
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sensor?.name.toString(),
                    style = MaterialTheme.typography.body1
                )
                sensor?.value?.let {
                    Text(
                        text = sensor.value.toString(),
                        style = MaterialTheme.typography.body2
                    )
                }
                sensor?.datepoint?.let {
                    Text(
                        text = sensor.datepoint.toString(),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(64.dp)
            ) {
                Image(
                    painter = if (sensor == null)
                        painterResource(id = R.drawable.logo)
                    else
                        painterResource(id = sensor.image),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(
                            CircleShape
                        )
                        .size(32.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}