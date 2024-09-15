package com.example.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.search.domain.model.TrackDataClass

class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    private var player: MediaPlayer? = null
    private var currentState: PlayerState = PlayerState.Default()
    private var currentUrl: String? = null

    private var onPreparedListener: (() -> Unit)? = null
    private var onCompletionListener: (() -> Unit)? = null

    override fun setDataSource(previewUrl: String) {
        currentUrl = previewUrl
        reset()
    }

    override fun prepareAsync() {
        player?.let {
            it.setDataSource(currentUrl)
            it.prepareAsync()
            currentState = PlayerState.Default()
        }
    }

    override fun setOnPreparedListener(onPrepared: () -> Unit) {
        onPreparedListener = onPrepared
        player?.setOnPreparedListener {
            currentState = PlayerState.Prepared()
            onPreparedListener?.invoke()
        }
    }

    override fun setOnCompletionListener(onCompletion: () -> Unit) {
        onCompletionListener = onCompletion
        player?.setOnCompletionListener {
            currentState = PlayerState.Completed()
            onCompletionListener?.invoke()
        }
    }

    override fun start() {
        player?.let {
            it.start()
            currentState = PlayerState.Playing(getCurrentPosition().toString())
        }
    }

    override fun stop() {
        player?.let {
            it.stop()
            currentState = PlayerState.Default()
        }
    }

    override fun isPlaying(): Boolean = player?.isPlaying ?: false

    override fun pause() {
        player?.let {
            if (it.isPlaying) {
                it.pause()
                currentState = PlayerState.Paused(getCurrentPosition().toString())
            }
        }
    }

    override fun release() {
        player?.release()
        player = null
        currentState = PlayerState.Default()
    }

    override fun getCurrentPosition(): Int = player?.currentPosition ?: 0

    override fun onFavoriteClick(track: TrackDataClass){
        track.isFavorite = !track.isFavorite
    }

    private fun reset() {
        release()
        player = MediaPlayer()
    }
}