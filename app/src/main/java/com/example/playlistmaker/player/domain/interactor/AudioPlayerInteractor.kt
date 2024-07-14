package com.example.playlistmaker.player.domain.interactor

import com.example.playlistmaker.player.ui.state.PlayerState

interface AudioPlayerInteractor {
    fun play()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
    fun getState(): PlayerState
    fun prepare(callbackPrepare: () -> Unit,
                callbackComplete: () -> Unit)
}