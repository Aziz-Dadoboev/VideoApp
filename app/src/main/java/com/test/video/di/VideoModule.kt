package com.test.video.di

import android.app.Application
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object VideoModule {

    @OptIn(UnstableApi::class)
    @Provides
    @ViewModelScoped
    fun providesVideoPlayer(app: Application): Player {
        return ExoPlayer.Builder(app)
            .setUseLazyPreparation(true)
            .build()
    }
}