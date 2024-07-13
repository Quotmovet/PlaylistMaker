package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.TrackDataClass

interface HistoryTrackRepositorySH {
    fun getTrackListFromSH(): Array<TrackDataClass>
    fun saveTrackListToSH(historyList: ArrayList<TrackDataClass>)
}