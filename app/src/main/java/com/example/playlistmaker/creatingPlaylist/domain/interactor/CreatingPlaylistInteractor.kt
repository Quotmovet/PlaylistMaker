package com.example.playlistmaker.creatingPlaylist.domain.interactor

import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.flow.Flow

interface CreatingPlaylistInteractor {

    fun addNewPlaylist(playlist: PlaylistDataClass): Flow<Long>         // Возвращает id добавленного плейлиста

    fun updatePlaylist(playlist: PlaylistDataClass): Flow<Int>

    fun saveImageToPrivateStorage(uri: String): String

    fun addTrackInPlaylist(track: TrackDataClass)
}