package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.flow.Flow

interface SearchTrackInteractor {
    suspend fun searchTrack(expression: String): Flow<Resource<List<TrackDataClass>>>
}