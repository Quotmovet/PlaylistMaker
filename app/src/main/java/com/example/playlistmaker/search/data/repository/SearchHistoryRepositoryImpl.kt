package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.search.data.dto.TrackDataClassDto
import com.example.playlistmaker.search.domain.repository.HistoryTrackRepositorySH
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.mapper.TrackMapper

class SearchHistoryRepositoryImpl(private val searchHistory: SearchHistoryRepository
): HistoryTrackRepositorySH {

    override fun getTrackListFromSH(): Array<TrackDataClass> {
        val tracksDto = searchHistory.getTracksFromHistory()
        return tracksDto.map { TrackMapper.mapTrackDataToDomain(it) }.toTypedArray()
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