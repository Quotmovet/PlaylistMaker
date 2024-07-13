package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.ui.activity.PlayerState

interface AudioPlayerInteractor {
    fun play()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
    fun getState(): PlayerState
    fun prepare(callbackPrepare: () -> Unit,
                callbackComplete: () -> Unit)
}