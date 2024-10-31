package com.example.playlistmaker.media.data.impl

import android.util.Log
import com.example.playlistmaker.creatingPlaylist.data.db.converter.PlaylistDbConverter
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.domain.db.PlaylistRepository
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlaylistRepositoryImpl (
    private val appDatabase: AppDatabase,
    private val convertor: PlaylistDbConverter,
): PlaylistRepository {

    override suspend fun getPlaylist(): Flow<List<PlaylistDataClass>> {
        return appDatabase.getPlaylistDao().getPlaylists().map { entityList ->
            entityList.map { playlistEntity ->
                updatePlaylistTrackCount(playlistEntity.id)
                convertor.map(playlistEntity)
            }
        }
    }

    override fun getPlaylistById(playlistId: Int): Flow<PlaylistDataClass> = flow {
        val playlist = appDatabase.getPlaylistDao().getPlaylistById(playlistId)
        updatePlaylistTrackCount(playlistId)
        emit(convertor.map(playlist))
    }

    override fun getPlaylistTrackList(playlistIdList: List<Int>): Flow<List<TrackDataClass>> = flow {
        try {
            val tracks = appDatabase.getPlaylistDao().getTracksFromPlaylists(playlistIdList)

            val trackDataList = tracks.map { trackEntity ->
                TrackDataClass(
                    trackName = trackEntity.trackName,
                    artistName = trackEntity.artistName,
                    trackTimeMillis = trackEntity.trackTimeMillis,
                    artworkUrl1100 = trackEntity.artworkUrl1100,
                    collectionName = trackEntity.collectionName,
                    releaseDate = trackEntity.releaseDate,
                    primaryGenreName = trackEntity.primaryGenreName,
                    country = trackEntity.country,
                    previewUrl = trackEntity.previewUrl,
                    trackId = trackEntity.trackId
                )
            }
            emit(trackDataList)
        } catch (e: Exception) {
            Log.e("PlaylistRepository", "Ошибка при получении треков", e)
            emit(emptyList())
        }
    }

    override fun deleteTrackById(trackId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.getPlaylistDao().deleteTrackById(trackId)
        }
    }

    override fun deletePlaylistById(playlistId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.getPlaylistDao().deletePlaylistById(playlistId)
        }
    }

    private fun updatePlaylistTrackCount(playlistId: Int) {
        val playlist = appDatabase.getPlaylistDao().getPlaylistById(playlistId)
        val trackIds = playlist.tracksListId.split(",").filter { it.isNotEmpty() }
        val newTrackCount = trackIds.size
        if (playlist.trackCount != newTrackCount) {
            playlist.trackCount = newTrackCount
            appDatabase.getPlaylistDao().updatePlaylist(playlist)
        }
    }
}