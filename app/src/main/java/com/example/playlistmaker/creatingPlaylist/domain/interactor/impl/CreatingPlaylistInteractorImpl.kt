package com.example.playlistmaker.creatingPlaylist.domain.interactor.impl

import com.example.playlistmaker.creatingPlaylist.data.db.converter.PlaylistDbConverter
import com.example.playlistmaker.creatingPlaylist.domain.repository.CreatingPlaylistRepository
import com.example.playlistmaker.creatingPlaylist.domain.interactor.CreatingPlaylistInteractor
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.player.data.db.converter.TrackFromPlaylistsEntityDbConvertor
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.flow.Flow

class CreatingPlaylistInteractorImpl(
    private val repository: CreatingPlaylistRepository,
    private val playlistConvertor: PlaylistDbConverter
): CreatingPlaylistInteractor {

    override fun addNewPlaylist(playlist: PlaylistDataClass): Flow<Long> {
        val playlistEntity = playlistConvertor.map(playlist)
        return repository.addNewPlaylist(playlistEntity)
    }

    override fun updatePlaylist(playlist: PlaylistDataClass): Flow<Int> {
        val playlistEntity = playlistConvertor.map(playlist)
        return repository.updatePlaylist(playlistEntity)
    }

    override fun saveImageToPrivateStorage(uri: String): String {
        return repository.saveImageToPrivateStorage(uri)
    }

    override fun addTrackInPlaylist(track: TrackDataClass) {
        val trackFromPlaylistsEntity = TrackFromPlaylistsEntityDbConvertor().map(track)
        repository.addTrackInPlaylist(trackFromPlaylistsEntity)
    }
}