package com.example.playlistmaker.player.domain.interactor.impl

import com.example.playlistmaker.media.data.db.converter.TrackDbConverter
import com.example.playlistmaker.media.domain.db.FavoritesRepository
import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.search.domain.model.TrackDataClass

class AudioPlayerInteractorImpl(
    private val audioPlayerRepository: AudioPlayerRepository,
    private val favoritesRepository: FavoritesRepository,
    private val trackDbConverter: TrackDbConverter,
) : AudioPlayerInteractor {

    override fun pause() = audioPlayerRepository.pause()

    override fun release() = audioPlayerRepository.release()

    override fun getCurrentPosition(): Int = audioPlayerRepository.getCurrentPosition()

    override fun isPlaying(): Boolean = audioPlayerRepository.isPlaying()

    override fun setDataSource(previewUrl: String) {
        audioPlayerRepository.setDataSource(previewUrl)
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

    override suspend fun onFavoriteClick(track: TrackDataClass) {
        val trackEntity = trackDbConverter.map(track)
        if (track.isFavorite) {
            favoritesRepository.deleteTrack(trackEntity)
        } else {
            favoritesRepository.insertTrack(trackEntity)
        }
        track.isFavorite = !track.isFavorite
    }

    override suspend fun isTrackFavorite(trackId: Int): Boolean {
        return favoritesRepository.isTrackFavorite(trackId)
    }
}