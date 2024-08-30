package com.example.playlistmaker.search.domain.interactor.impl

import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.search.domain.interactor.SearchTrackInteractor
import com.example.playlistmaker.search.domain.repository.SearchTrackRepository
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.flow.Flow

class SearchTrackInteractorImpl(
    private val searchTrackRepository: SearchTrackRepository
) : SearchTrackInteractor {

        override suspend fun searchTrack(expression: String): Flow<Resource<List<TrackDataClass>>> {
        return searchTrackRepository.searchTrack(expression)
    }
}