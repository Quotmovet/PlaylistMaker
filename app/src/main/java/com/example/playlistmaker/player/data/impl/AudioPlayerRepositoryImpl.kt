package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.AudioPlayerRepository
import com.example.playlistmaker.player.ui.activity.PlayerState

class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    private var player: MediaPlayer? = null
    private var currentState = PlayerState.BEFORE_THE_START
    private var currentUrl: String? = null

    override fun play() {
        when (currentState) {
            PlayerState.PREPARED, PlayerState.PAUSED -> {
                player?.start()
                currentState = PlayerState.STARTED
            }
            PlayerState.COMPLETED -> {
                preparePlayer({
                    player?.start()
                    currentState = PlayerState.STARTED
                }, {
                    currentState = PlayerState.COMPLETED
                })
            }
            else -> { /* Не делаем ничего */ }
        }
    }

    override fun pause() {
        player?.let {
            if (currentState == PlayerState.STARTED) {
                it.pause()
                currentState = PlayerState.PAUSED
            }
        }
    }

    override fun release() {
        player?.release()
        player = null
        currentState = PlayerState.BEFORE_THE_START
    }

    override fun currentPosition(): Int = player?.currentPosition ?: 0

    override fun getState(): PlayerState = currentState

    override fun prepare(previewUrl: String,
                         callbackOnPrepared: () -> Unit,
                         callbackOnCompletion: () -> Unit) {

        currentUrl = previewUrl
        preparePlayer(callbackOnPrepared, callbackOnCompletion)
    }

    private fun preparePlayer(callbackOnPrepared: () -> Unit,
                              callbackOnCompletion: () -> Unit) {

        release()
        player = MediaPlayer().apply {
            setDataSource(currentUrl)
            setOnPreparedListener {
                currentState = PlayerState.PREPARED
                callbackOnPrepared()
            }
            setOnCompletionListener {
                currentState = PlayerState.COMPLETED
                callbackOnCompletion()
            }
            prepareAsync()
        }
        currentState = PlayerState.INITIALIZING
    }

    override fun reset() {
        player?.let {
            it.reset()
            currentState = PlayerState.BEFORE_THE_START
        }
    }
}