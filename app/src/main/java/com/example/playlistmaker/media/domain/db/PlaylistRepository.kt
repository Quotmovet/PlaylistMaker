package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun getPlaylist(): Flow<List<PlaylistDataClass>>

    fun getPlaylistById(playlistId: Int): Flow<PlaylistDataClass>

    fun getPlaylistTrackList(playlistIdList: List<Int>): Flow<List<TrackDataClass>>

    fun deleteTrackById (trackId:Int)

    fun deletePlaylistById (playlistId: Int)
}