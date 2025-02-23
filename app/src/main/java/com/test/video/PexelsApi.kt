package com.test.video

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PexelsApi {
    @GET("videos/popular")
    suspend fun getPopularVideos(
        @Header("Authorization") apiKey: String,
        @Query("per_page") perPage: Int = 10
    ): PexelsResponse
}
