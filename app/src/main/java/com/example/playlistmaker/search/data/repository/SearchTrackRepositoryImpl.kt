package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.dto.SearchTrackRequest
import com.example.playlistmaker.search.data.dto.SearchTrackResponse
import com.example.playlistmaker.search.domain.repository.SearchTrackRepository
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.mapper.TrackMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase): SearchTrackRepository {

    override suspend fun searchTrack(
        expression: String) : Flow<Resource<List<TrackDataClass>>> = flow {

        val response = networkClient.doRequest(SearchTrackRequest(expression))

        return@flow when (response.resultCode) {

            -1 -> {
                emit(Resource.Error(isFailed = false))
            }

            200 -> {
                with(response as SearchTrackResponse) {
                    val favoritesTrackIds = appDatabase.trackDao().getFavoriteTracksIds()
                    val data = results.map { dto ->
                        val track = TrackMapper.mapTrackDataToDomain(dto)
                        track.isFavorite = track.trackId in favoritesTrackIds
                        track
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error(isFailed = true))
            }
        }
    }
}