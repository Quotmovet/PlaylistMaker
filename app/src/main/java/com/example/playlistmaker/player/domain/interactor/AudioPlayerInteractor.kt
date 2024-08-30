package com.example.playlistmaker.player.domain.interactor

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
}