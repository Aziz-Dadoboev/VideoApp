package com.test.video.data

import android.os.Parcelable
import androidx.media3.common.MediaItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoItem(
    val contentUri: String,
    val name: String,
    val thumbnail: String,
    val duration: String
) : Parcelable {
    fun toMediaItem(): MediaItem {
        return MediaItem.fromUri(contentUri)
    }
}