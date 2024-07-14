package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.model.TrackDataClass

interface SearchTrackHistoryInteractor {
    fun getHistoryList(): ArrayList<TrackDataClass>
    fun saveHistoryList()
    fun addTrackToHistoryList(track: TrackDataClass)
    fun clearHistoryList()
    fun transferToTop(track: TrackDataClass): Int
}