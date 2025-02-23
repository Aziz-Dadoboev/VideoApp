package com.test.video

data class PexelsResponse(
    val videos: List<PexelsVideo>
)
data class PexelsVideo(
    val id: Int,
    val image: String,
    val duration: Int,
    val video_files: List<PexelsVideoFile>
)

data class PexelsVideoFile(
    val quality: String,
    val file_type: String,
    val link: String
)