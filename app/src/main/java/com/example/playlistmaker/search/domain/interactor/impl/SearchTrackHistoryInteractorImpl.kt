package com.example.playlistmaker.search.domain.interactor.impl

import com.example.playlistmaker.search.domain.repository.HistoryTrackRepositorySH
import com.example.playlistmaker.search.domain.interactor.SearchTrackHistoryInteractor
import com.example.playlistmaker.search.domain.model.TrackDataClass

class SearchTrackHistoryInteractorImpl(private val historyTrackRepositorySH: HistoryTrackRepositorySH?
): SearchTrackHistoryInteractor {

    private val historyList: ArrayList<TrackDataClass> = ArrayList(
        historyTrackRepositorySH?.getTrackListFromSH()?.toList() ?: emptyList())

    override fun getHistoryList(): ArrayList<TrackDataClass> {
        return historyList
    }

    override fun saveHistoryList() {
        historyTrackRepositorySH?.saveTrackListToSH(historyList)
    }

    override fun addTrackToHistoryList(track: TrackDataClass) {
        val index = historyList.indexOfFirst { it.trackId == track.trackId }

        if (historyList.size < 10) {
            if (index == -1) {
                historyList.add(0, track)
            } else {
                shiftElementToTopOfHistoryList(index)
            }
        } else {
            if (index == -1) {
                historyList.removeAt(historyList.lastIndex)
                historyList.add(0, track)
            } else {
                shiftElementToTopOfHistoryList(index)
            }
        }
    }

    override fun clearHistoryList() {
        historyList.clear()
    }

    override fun transferToTop(track: TrackDataClass): Int {
        val index = historyList.indexOfFirst { it.trackId == track.trackId }
        if (index != 0) {
            shiftElementToTopOfHistoryList(index)
        }
        return index
    }

    private fun shiftElementToTopOfHistoryList(index: Int) {
        val trackToMove = historyList[index]
        historyList.removeAt(index)
        historyList.add(0, trackToMove)
    }

    override suspend fun updateFavoriteStatus(trackId: Int, isFavorite: Boolean) {
        val track = historyList.find { it.trackId == trackId }
        track?.let {
            it.isFavorite = isFavorite
            saveHistoryList()
        }
    }
}