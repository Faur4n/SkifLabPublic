package com.skifapp.skiflab.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.keygenqt.modifier.noRippleClickable
import com.skifapp.skiflab.R

class HelpScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.help_title),
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
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.background,
                )
            },
            modifier = Modifier
                .navigationBarsWithImePadding()
                .statusBarsPadding()
        ) {

            Column(
                modifier = Modifier.verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(
                            rememberScrollState()
                        )
                        .padding(horizontal = 12.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .width(325.dp)
                                .height(200.dp)
                                .background(
                                    Color(0xFFC6EBFE),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .noRippleClickable {
                                    navigator.push(VideoScreen(Video.First))
                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.technic),
                                contentDescription = null
                            )
                        }
                        Text(
                            text = "Как подключить обборудование",
                            style = MaterialTheme.typography.subtitle1
                        )
                        Text(text = "Всего за 3 минуты", style = MaterialTheme.typography.caption)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Card(
                            modifier = Modifier
                                .width(325.dp)
                                .height(200.dp)
                                .background(
                                    Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .noRippleClickable {
                                    navigator.push(VideoScreen(Video.Second))
                                }
                        ) {
                            Box(Modifier.fillMaxSize()) {
                                Image(
                                    painter = painterResource(id = R.drawable.sensor),
                                    contentDescription = null,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                        Text(
                            text = "Как настроить ваши счетчики",
                            style = MaterialTheme.typography.subtitle1
                        )
                        Text(
                            text = "Проще всего - в приложении",
                            style = MaterialTheme.typography.caption
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .width(325.dp)
                                .height(200.dp)
                                .background(
                                    Color(0xFFC6ECEB),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .noRippleClickable {
                                    navigator.push(VideoScreen(Video.Third))
                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.houses),
                                contentDescription = null,
                            )
                        }
                        Text(
                            text = "Подключите ваш дом к системе",
                            style = MaterialTheme.typography.subtitle1
                        )
                        Text(
                            text = "С вами свяжется оператор SKIF PRO",
                            style = MaterialTheme.typography.caption
                        )
                    }
                }


                Column(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Помошь по разделам", style = MaterialTheme.typography.h5)

                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    MaterialTheme.colors.primarySurface.copy(
                                        alpha = 0.4f
                                    )
                                )
                                .padding(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.mobile_app),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = Color.White
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(
                                4.dp,
                                Alignment.CenterVertically
                            ), modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 12.dp)
                        ) {
                            Text(text = "С чего начать", style = MaterialTheme.typography.body1)
                            Text(
                                text = "Справка по работе с мобильным приложением SKIF ЖКХ",
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    MaterialTheme.colors.primarySurface.copy(
                                        alpha = 0.4f
                                    )
                                )
                                .padding(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.pressure_gauge),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = Color.White
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(
                                4.dp,
                                Alignment.CenterVertically
                            ), modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 12.dp)
                        ) {
                            Text(
                                text = "Настройка обборудования",
                                style = MaterialTheme.typography.body1
                            )
                            Text(
                                text = "Частые вопросы по настройке и эксплуатации счетчиков SKIF",
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    MaterialTheme.colors.primarySurface.copy(
                                        alpha = 0.4f
                                    )
                                )
                                .padding(24.dp)

                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.question),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = Color.White
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 12.dp)
                        ) {
                            Text(
                                text = "Как связаться с поддежкой",
                                style = MaterialTheme.typography.body1
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(
                                text = "Тех. поддержка SKIF поможет с любой проблемой",
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }

                }
            }
        }
    }
}