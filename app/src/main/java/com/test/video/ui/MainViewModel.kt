package com.test.video.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.test.video.data.VideoItem
import com.test.video.data.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    val player: Player
): ViewModel() {

    private val _videos: StateFlow<List<VideoItem>> = videoRepository.videos
    val videos: StateFlow<List<VideoItem>> = _videos

    init {
        player.prepare()
        viewModelScope.launch {
            videoRepository.fetchVideos()
        }
    }

    fun playVideo(uri: String) {
        val mediaItem = videos.value.find {
            it.contentUri == uri
        }?.toMediaItem() ?: return
        player.setMediaItem(mediaItem)
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
