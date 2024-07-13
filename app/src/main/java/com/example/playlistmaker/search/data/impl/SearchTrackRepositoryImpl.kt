package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.di.Resource
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.dto.SearchTrackRequest
import com.example.playlistmaker.search.data.dto.SearchTrackResponse
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.search.domain.model.TrackDataClass

class SearchTrackRepositoryImpl(private val networkClient: NetworkClient):
    SearchTrackRepository {

    override fun searchTrack(expression: String): Resource<List<TrackDataClass>> {
        val response = networkClient.doRequest(SearchTrackRequest(expression))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error(isFailed = false)
            }
            200 -> {
                Resource.Success((response as SearchTrackResponse).results.map {
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
                })
            }
            else -> {
                Resource.Error(isFailed = true)
            }
        }
    }
}