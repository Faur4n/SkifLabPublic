package com.skifapp.skiflab.home

import android.net.Uri
import android.os.Parcelable
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.skifapp.skiflab.R
import kotlinx.parcelize.Parcelize


@Parcelize
enum class Video(val uri: Uri) : Parcelable {
    First(Uri.parse("android.resource://com.skifapp.skiflab/raw/video1")),
    Second(Uri.parse("android.resource://com.skifapp.skiflab/raw/video2")),
    Third(Uri.parse("android.resource://com.skifapp.skiflab/raw/video3"))
}

class VideoScreen(
    private val video: Video
) : AndroidScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val exoPlayer = remember {
            ExoPlayer.Builder(context).build().apply {
                val item = MediaItem.Builder()
                    .setUri(video.uri)
                    .build()

                this.setMediaItem(item)
                this.prepare()
                this.playWhenReady = true
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                                contentDescription = null,
                                tint = MaterialTheme.colors.background,
                            )
                        }
                    },
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.onBackground,
                )
            },
            modifier = Modifier
                .navigationBarsWithImePadding()
                .statusBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.onBackground)
            ) {
                DisposableEffect(
                    AndroidView(factory = { context ->
                        PlayerView(context).apply {
                            player = exoPlayer
                            layoutParams =
                                FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams
                                        .MATCH_PARENT,
                                    ViewGroup.LayoutParams
                                        .MATCH_PARENT
                                )
                        }
                    })
                ) {
                    onDispose {
                        exoPlayer.release()
                    }
                }

            }
        }
    }
}