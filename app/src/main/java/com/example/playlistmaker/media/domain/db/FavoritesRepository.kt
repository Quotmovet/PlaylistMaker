package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun insertTrack(track : TrackEntity)
    suspend fun deleteTrack(track : TrackEntity)
    suspend fun isTrackFavorite(trackId: Int): Boolean
    fun getTracks() : Flow<List<TrackDataClass>>
}