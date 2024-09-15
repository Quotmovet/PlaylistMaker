package com.example.playlistmaker.player.domain.repository

import com.example.playlistmaker.search.domain.model.TrackDataClass

interface AudioPlayerRepository {
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
    fun onFavoriteClick(track: TrackDataClass)
}