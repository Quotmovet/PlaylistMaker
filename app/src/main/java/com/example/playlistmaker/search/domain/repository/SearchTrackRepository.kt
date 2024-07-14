package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.search.domain.model.TrackDataClass

interface SearchTrackRepository {
    fun searchTrack(expression: String): Resource<List<TrackDataClass>>
}