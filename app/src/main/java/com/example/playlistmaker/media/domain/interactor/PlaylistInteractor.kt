package com.example.playlistmaker.media.domain.interactor

import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getPlaylists(): Flow<List<PlaylistDataClass>>
}