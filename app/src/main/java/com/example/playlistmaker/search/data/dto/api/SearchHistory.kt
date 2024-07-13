package com.example.playlistmaker.search.data.dto.api

import com.example.playlistmaker.search.data.dto.models.TrackDataClassDto

interface SearchHistory {
    fun getTracksFromHistory(): Array<TrackDataClassDto>
    fun saveTracksToHistory(tracks: ArrayList<TrackDataClassDto>)
}