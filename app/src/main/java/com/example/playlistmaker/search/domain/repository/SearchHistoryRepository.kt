package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.data.dto.TrackDataClassDto

interface SearchHistoryRepository {
    fun getTracksFromHistory(): Array<TrackDataClassDto>
    fun saveTracksToHistory(tracks: ArrayList<TrackDataClassDto>)
}