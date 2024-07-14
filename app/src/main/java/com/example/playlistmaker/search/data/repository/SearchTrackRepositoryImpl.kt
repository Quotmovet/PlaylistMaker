package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.dto.SearchTrackRequest
import com.example.playlistmaker.search.data.dto.SearchTrackResponse
import com.example.playlistmaker.search.domain.repository.SearchTrackRepository
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.mapper.TrackMapper

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
                    TrackMapper.mapTrackDataToDomain(it)
                })
            }
            else -> {
                Resource.Error(isFailed = true)
            }
        }
    }
}