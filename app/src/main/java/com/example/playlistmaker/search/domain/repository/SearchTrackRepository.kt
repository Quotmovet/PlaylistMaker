package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.flow.Flow

interface SearchTrackRepository {
    suspend fun searchTrack(expression: String): Flow<Resource<List<TrackDataClass>>>
}