package com.test.video.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.test.video.data.VideoItem
import com.test.video.data.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    val player: Player
): ViewModel() {

    private val _videos: StateFlow<List<VideoItem>> = videoRepository.videos
    val videos: StateFlow<List<VideoItem>> = _videos

    val errorMessage: StateFlow<String?> = videoRepository.errorMessage

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    var currentVideoUrl: String? = null
        private set
    private var lastPosition: Long = 0L
    var isPlaying = false
        private set

    init {
        player.prepare()
        fetchVideos()
    }

    fun fetchVideos() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                videoRepository.fetchVideos()
            } catch (e: Exception) {
                Log.e("MainViewModel", "Ошибка при загрузке видео: ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }
    fun playVideo(uri: String) {
        val mediaItem = videos.value.find {
            it.contentUri == uri
        }?.toMediaItem() ?: return
        player.setMediaItem(mediaItem)
        player.prepare()

        if (lastPosition > 0) {
            player.seekTo(lastPosition)
        }

        if (isPlaying) {
            player.playWhenReady = true
        } else {
            player.playWhenReady = false
        }

        currentVideoUrl = uri
    }

    fun saveState() {
        lastPosition = player.currentPosition
        isPlaying = player.playWhenReady
    }

    override fun onCleared() {
        super.onCleared()
        saveState()
        player.release()
    }
}
