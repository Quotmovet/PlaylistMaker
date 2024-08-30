package com.example.playlistmaker.player.domain.interactor.impl

import android.util.Log
import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.mapper.TrackMapper

class AudioPlayerInteractorImpl(
    trackDataClass: TrackDataClass,
    private val audioPlayerRepository: AudioPlayerRepository
) : AudioPlayerInteractor {

    init {
        Log.d("AudioPlayerInteractorImpl", "Interactor initialized with track: $trackDataClass")
    }

    private val uiTrack = TrackMapper.mapTrackDomainToUi(trackDataClass)

    override fun pause() = audioPlayerRepository.pause()

    override fun release() = audioPlayerRepository.release()

    override fun getCurrentPosition(): Int = audioPlayerRepository.getCurrentPosition()

    override fun isPlaying(): Boolean = audioPlayerRepository.isPlaying()

    override fun setDataSource(previewUrl: String) {
        audioPlayerRepository.setDataSource(uiTrack.previewUrl)
    }

    override fun start() {
        audioPlayerRepository.start()
    }

    override fun stop() {
        audioPlayerRepository.stop()
    }

    override fun setOnPreparedListener(onPrepared: () -> Unit) {
        audioPlayerRepository.setOnPreparedListener(onPrepared)
    }

    override fun setOnCompletionListener(onCompletion: () -> Unit) {
        audioPlayerRepository.setOnCompletionListener(onCompletion)
    }

    override fun prepareAsync() {
        audioPlayerRepository.prepareAsync()
    }
}