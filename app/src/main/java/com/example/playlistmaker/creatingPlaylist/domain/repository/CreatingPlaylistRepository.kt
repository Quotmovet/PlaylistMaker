package com.example.playlistmaker.creatingPlaylist.domain.repository

import com.example.playlistmaker.creatingPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.player.data.db.entity.TrackFromPlaylistsEntity
import kotlinx.coroutines.flow.Flow

interface CreatingPlaylistRepository {

    fun addNewPlaylist(playlist: PlaylistEntity): Flow<Long>

    fun updatePlaylist(playlist: PlaylistEntity): Flow<Int>

    fun saveImageToPrivateStorage(uri: String): String

    fun addTrackInPlaylist(track: TrackFromPlaylistsEntity)
}