package com.example.playlistmaker.media.data.impl

import com.example.playlistmaker.creatingPlaylist.data.db.converter.PlaylistDbConverter
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.domain.db.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl (
    private val appDatabase: AppDatabase,
    private val convertor: PlaylistDbConverter,
): PlaylistRepository {

    override suspend fun getPlaylist(): Flow<List<PlaylistDataClass>> {
        return appDatabase.getPlaylistDao().getPlaylists().map {
            entityList -> entityList.map { convertor.map(it) } }
    }
}