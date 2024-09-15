package com.example.playlistmaker.player.domain.interactor

import com.example.playlistmaker.search.domain.model.TrackDataClass

interface AudioPlayerInteractor {
    fun setDataSource(previewUrl: String)
    fun prepareAsync()
    fun setOnPreparedListener(onPrepared: () -> Unit)
    fun setOnCompletionListener(onCompletion: () -> Unit)
    fun start()
    fun stop()
    fun isPlaying(): Boolean
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
    suspend fun onFavoriteClick(track: TrackDataClass)
    suspend fun isTrackFavorite(trackId: Int): Boolean
}