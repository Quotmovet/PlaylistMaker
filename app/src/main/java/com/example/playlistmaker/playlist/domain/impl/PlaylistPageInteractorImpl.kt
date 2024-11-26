package com.example.playlistmaker.playlist.domain.impl

import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.media.domain.db.PlaylistRepository
import com.example.playlistmaker.playlist.domain.PlaylistPageInteractor
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.flow.Flow

class PlaylistPageInteractorImpl(
    private val repository: PlaylistRepository
): PlaylistPageInteractor {

    override fun getPlaylistById(playlistId: Int): Flow<PlaylistDataClass> {
        return repository.getPlaylistById(playlistId)
    }

    override fun getPlaylistTrackList(playlistIdList: List<Int>): Flow<List<TrackDataClass>> {
        return repository.getPlaylistTrackList(playlistIdList)
    }

    override fun deletePlaylistById(playlistId: Int) {
        repository.deletePlaylistById(playlistId)
    }
}