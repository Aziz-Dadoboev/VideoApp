package com.test.video

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController

@Composable
fun VideoPlayerScreen(
    videoUrl: String,
    navController: NavController
) {
    val viewModel = hiltViewModel<MainViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current
    var lifecycleEvent by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycleEvent = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    viewModel.player.setMediaItem(MediaItem.fromUri(videoUrl))
                    viewModel.player.prepare()
                    viewModel.player.playWhenReady = true
                    player = viewModel.player
                }
            },
            update = {
                when (lifecycleEvent) {
                    Lifecycle.Event.ON_PAUSE -> {
                        it.player?.pause()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        it.player?.play()
                    }
                    else -> Unit
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(16 / 9f)
        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(
//            onClick = { navController.popBackStack() },
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        ) {
//            Text("Назад к списку")
//        }
    }
}
