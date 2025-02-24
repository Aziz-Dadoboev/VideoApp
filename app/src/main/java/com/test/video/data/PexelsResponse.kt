package com.test.video.data

data class PexelsResponse(
    val page: Int,
    val per_page: Int,
    val media: List<Video>,
    val total_results: Int,
    val id: String
)

data class Video(
    val type: String,
    val id: Int,
    val width: Int,
    val height: Int,
    val duration: Int,
    val url: String,
    val image: String,
    val video_files: List<VideoFile>,
    val video_pictures: List<VideoPicture>,
    val user: User
)

data class VideoFile(
    val id: Int,
    val quality: String,
    val file_type: String,
    val width: Int,
    val height: Int,
    val fps: Float,
    val link: String,
    val size: Int
)

data class VideoPicture(
    val id: Int,
    val nr: Int,
    val picture: String
)

data class User(
    val id: Int,
    val name: String,
    val url: String
)
