package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.data.AudioPlayerRepository
import com.example.playlistmaker.player.domain.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.models.TrackModel
import com.example.playlistmaker.player.ui.activity.PlayerState

class AudioPlayerInteractorImpl(
    private val trackPlayer: TrackModel,
    private val audioPlayerRepository: AudioPlayerRepository
) : AudioPlayerInteractor {

    override fun play() = audioPlayerRepository.play()

    override fun pause() = audioPlayerRepository.pause()

    override fun release() = audioPlayerRepository.release()

    override fun getCurrentPosition(): Int = audioPlayerRepository.currentPosition()

    override fun getState(): PlayerState = audioPlayerRepository.getState()

    override fun prepare(callbackPrepare: () -> Unit,
                         callbackComplete: () -> Unit) {

        audioPlayerRepository.prepare(
            previewUrl = trackPlayer.previewUrl,
            callbackOnPrepared = callbackPrepare,
            callbackOnCompletion = callbackComplete
        )
    }
}