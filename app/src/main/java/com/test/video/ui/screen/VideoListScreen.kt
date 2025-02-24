package com.test.video.ui.screen

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.test.video.data.VideoItem
import com.test.video.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoListScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<MainViewModel>()
    val videoItems by viewModel.videos.collectAsState(emptyList())
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val refreshVideos = {
        viewModel.fetchVideos()
    }

    if (errorMessage != null) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { refreshVideos() },
            modifier = Modifier
        ) {
            ErrorScreen(message = errorMessage ?: "Неизвестная ошибка") {
                viewModel.fetchVideos()
            }
        }
    } else {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { refreshVideos() },
            modifier = Modifier
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(videoItems) { item ->
                    VideoListItem(video = item) {
                        navController.navigate("video_player/${Uri.encode(item.contentUri)}")
                    }
                }
            }
        }
    }
}

@Composable
fun VideoListItem(
    video: VideoItem,
    onClick: () -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = video.thumbnail,
            contentDescription = "Thumbnail",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = video.name, fontWeight = FontWeight.Bold)
            Text(text = "Duration: ${video.duration}", color = Color.Gray)
        }
    }
}
