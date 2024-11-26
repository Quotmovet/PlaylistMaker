package com.example.playlistmaker.media.domain.interactor.impl

import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.media.domain.db.PlaylistRepository
import com.example.playlistmaker.media.domain.interactor.PlaylistInteractor
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository): PlaylistInteractor {

    override suspend fun getPlaylists(): Flow<List<PlaylistDataClass>> {
        return repository.getPlaylist()
    }

    override fun deleteTrackById(trackId: Int) {
        repository.deleteTrackById(trackId)
    }
}