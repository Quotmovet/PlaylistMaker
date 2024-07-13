package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.di.Resource
import com.example.playlistmaker.search.domain.model.TrackDataClass

interface TrackInteractor {
    suspend fun searchTrack(expression: String): Resource<List<TrackDataClass>>
}