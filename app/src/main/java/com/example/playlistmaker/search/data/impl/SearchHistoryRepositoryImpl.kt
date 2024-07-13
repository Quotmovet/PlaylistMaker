package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.dto.api.SearchHistory
import com.example.playlistmaker.search.data.dto.models.TrackDataClassDto
import com.example.playlistmaker.search.domain.api.HistoryTrackRepositorySH
import com.example.playlistmaker.search.domain.model.TrackDataClass

class SearchHistoryRepositoryImpl(private val searchHistory: SearchHistory
): HistoryTrackRepositorySH {

    override fun getTrackListFromSH(): Array<TrackDataClass> {
        val tracksDto = searchHistory.getTracksFromHistory()
        val tracks: Array<TrackDataClass> =  tracksDto.map {
            TrackDataClass(
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl1100 = it.artworkUrl1100,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                previewUrl = it.previewUrl,
                trackId = it.trackId
                )
        }.toTypedArray()

        return tracks
    }

    override fun saveTrackListToSH(historyList: ArrayList<TrackDataClass>) {
        val trackDataClassDto = historyList.map {
            TrackDataClassDto(
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl1100 = it.artworkUrl1100,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                previewUrl = it.previewUrl,
                trackId = it.trackId
            )
        }
        searchHistory.saveTracksToHistory(ArrayList(trackDataClassDto))
    }
}