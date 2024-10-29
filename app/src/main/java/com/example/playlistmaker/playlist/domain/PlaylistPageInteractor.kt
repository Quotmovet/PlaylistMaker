package com.example.playlistmaker.playlist.domain

import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.flow.Flow

interface PlaylistPageInteractor {

    fun getPlaylistById (playlistId: Int): Flow<PlaylistDataClass>

    fun getPlaylistTrackList(playlistIdList: List<Int>): Flow<List<TrackDataClass>>

    fun deletePlaylistById (playlistId: Int)

}