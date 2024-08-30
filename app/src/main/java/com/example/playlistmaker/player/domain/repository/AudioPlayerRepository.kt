package com.example.playlistmaker.player.domain.repository

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
}