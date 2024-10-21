package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun getPlaylist(): Flow<List<PlaylistDataClass>>
}