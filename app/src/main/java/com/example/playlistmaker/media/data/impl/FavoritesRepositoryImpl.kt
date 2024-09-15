package com.example.playlistmaker.media.data.impl

import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.converters.TrackDbConverter
import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.domain.db.FavoritesRepository
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter,
) : FavoritesRepository {

    // Добавляет трек в избранное
    override suspend fun insertTrack(track: TrackEntity) {
        withContext(Dispatchers.IO) {
            appDatabase.trackDao().insertFavoritesTracks(listOf(track))
        }
    }

    // Удаляет трек из избранного
    override suspend fun deleteTrack(track: TrackEntity) {
        withContext(Dispatchers.IO) {
            appDatabase.trackDao().deleteFavoriteTrackById(track.trackId)
        }
    }

    // Проверяет, добавлен ли трек в избранное
    override suspend fun isTrackFavorite(trackId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            appDatabase.trackDao().isTrackFavorite(trackId)
        }
    }

    // Возвращает список избранных треков
    override fun getTracks(): Flow<List<TrackDataClass>> = flow {
        val tracks = appDatabase.trackDao().getFavoritesTracks()
        emit(convertFromTrackEntity(tracks))
    }

    // Конвертирует список избранных треков из Entity в DataClass
    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<TrackDataClass> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }
}