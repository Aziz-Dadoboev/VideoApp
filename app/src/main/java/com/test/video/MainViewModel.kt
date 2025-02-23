package com.test.video

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val player: Player,
    private val pexelsApi: PexelsApi
): ViewModel() {

//    private val apiKey = BuildConfig.PEXELS_API_KEY
    private val apiKey = "g6ulP6T1dSMzIl1mTTevMf1YmMknyYAROBjFeMO1L2KxoUvD6f0ZE1C4"

    private val _videos = savedStateHandle.getLiveData("videos", emptyList<VideoItem>())
    val videos: LiveData<List<VideoItem>> = _videos

    init {
        player.prepare()
        if (_videos.value.isNullOrEmpty()) {
            fetchVideos()
        }
    }

    private fun fetchVideos() {
        viewModelScope.launch {
            try {
                val response = pexelsApi.getPopularVideos(apiKey)
                val newVideos = response.videos.map { video ->
                    val bestQualityVideo = video.video_files
                        .filter { it.file_type == "video/mp4" }
                        .maxByOrNull { it.quality }

                    VideoItem(
                        contentUri = bestQualityVideo?.link ?: "",
                        name = "Видео ${video.id}",
                        thumbnail = video.image,
                        duration = formatDuration(video.duration)
                    )
                }
                savedStateHandle["videos"] = newVideos

            } catch (e: Exception) {
                Log.e("MainViewModel", "Ошибка загрузки видео: ${e.message}")
            }
        }
    }

    fun playVideo(uri: String) {
        val mediaItem = videos.value?.find {
            it.contentUri == uri
        }?.toMediaItem() ?: return
        player.setMediaItem(mediaItem)
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    @SuppressLint("DefaultLocale")
    private fun formatDuration(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", minutes, secs)
    }
}
