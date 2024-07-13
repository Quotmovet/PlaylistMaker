package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.ui.activity.PlayerState

interface AudioPlayerRepository {
    fun play()
    fun pause()
    fun release()
    fun currentPosition(): Int
    fun getState(): PlayerState
    fun prepare(previewUrl: String,
                callbackOnPrepared: () -> Unit,
                callbackOnCompletion: () -> Unit)
    fun reset()
}