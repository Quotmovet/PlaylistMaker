package com.example.playlistmaker.search.data.datasource

import com.example.playlistmaker.search.data.dto.TrackDataClassDto

interface SearchHistoryLocalDataSource {
    fun getTracksFromHistory(): Array<TrackDataClassDto>
    fun saveTracksToHistory(tracks: ArrayList<TrackDataClassDto>)
}