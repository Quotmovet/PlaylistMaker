package com.example.playlistmaker.media.domain.interactor

import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    fun getFavoriteTracks(): Flow<List<TrackDataClass>>
}