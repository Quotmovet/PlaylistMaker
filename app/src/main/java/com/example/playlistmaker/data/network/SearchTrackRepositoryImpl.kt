package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.SearchTrackResponse
import com.example.playlistmaker.data.dto.SearchTrackRequest
import com.example.playlistmaker.domain.api.SearchTrackRepository
import com.example.playlistmaker.domain.models.TrackDataClass

class SearchTrackRepositoryImpl(private val networkClient: NetworkClient):
    SearchTrackRepository {

    override fun searchTrack(expression: String): List<TrackDataClass> {
        val response = networkClient.doRequest(SearchTrackRequest(expression))

        if (response.resultCode == 200) {
            return (response as SearchTrackResponse).results.map {
                TrackDataClass(
                    trackId = it.trackId,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTimeMillis = it.trackTimeMillis,
                    artworkUrl1100 = it.artworkUrl1100,
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    previewUrl = it.previewUrl
                )
            }
        } else {
            return emptyList()
        }
    }
}