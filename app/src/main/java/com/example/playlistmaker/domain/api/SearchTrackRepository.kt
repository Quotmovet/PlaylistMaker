package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TrackDataClass

interface SearchTrackRepository {
    fun searchTrack(expression: String): List<TrackDataClass>
}