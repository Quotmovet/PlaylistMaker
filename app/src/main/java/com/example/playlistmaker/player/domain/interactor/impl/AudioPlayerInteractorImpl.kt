package com.example.playlistmaker.player.domain.interactor.impl

import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.mapper.TrackMapper

class AudioPlayerInteractorImpl(
    private val trackDataClass: TrackDataClass,
    private val audioPlayerRepository: AudioPlayerRepository
) : AudioPlayerInteractor {

    override fun play() = audioPlayerRepository.play()

    override fun pause() = audioPlayerRepository.pause()

    override fun release() = audioPlayerRepository.release()

    override fun getCurrentPosition(): Int = audioPlayerRepository.currentPosition()

    override fun getState(): PlayerState = audioPlayerRepository.getState()

    override fun prepare(callbackPrepare: () -> Unit,
                         callbackComplete: () -> Unit) {

        val trackDomain = TrackMapper.mapTrackDomainToUi(trackDataClass)
        audioPlayerRepository.prepare(
            previewUrl = trackDomain.previewUrl,
            callbackOnPrepared = callbackPrepare,
            callbackOnCompletion = callbackComplete
        )
    }
}