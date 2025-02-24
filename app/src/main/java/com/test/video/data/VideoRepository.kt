package com.test.video.data

import android.annotation.SuppressLint
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepository @Inject constructor(
    private val pexelsApi: PexelsApi
) {
    private val apiKey = "g6ulP6T1dSMzIl1mTTevMf1YmMknyYAROBjFeMO1L2KxoUvD6f0ZE1C4"
    private val _videos = MutableStateFlow<List<VideoItem>>(emptyList())
    val videos: StateFlow<List<VideoItem>> = _videos.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    suspend fun fetchVideos() {
        try {
            val response = pexelsApi.getPopularVideos(apiKey)
            _videos.value = response.media.map { video ->
                val firstVideo = video.video_files
                    .firstOrNull { it.file_type == "video/mp4" }
                VideoItem(
                    contentUri = firstVideo?.link ?: "",
                    name = "Видео ${video.id}",
                    thumbnail = video.image,
                    duration = formatDuration(video.duration)
                )
            }
            _errorMessage.value = null
        } catch (e: Exception) {
            Log.e("VideoRepository", "Ошибка загрузки видео: ${e.message}")
            _errorMessage.value = "Ошибка загрузки данных: ${e.message}"
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatDuration(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", minutes, secs)
    }
}
