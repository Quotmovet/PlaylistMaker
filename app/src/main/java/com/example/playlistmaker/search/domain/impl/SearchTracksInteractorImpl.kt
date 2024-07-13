package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.di.Resource
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.search.domain.model.TrackDataClass

class SearchTracksInteractorImpl(
    private val searchTrackRepository: SearchTrackRepository) : TrackInteractor {

        override suspend fun searchTrack(expression: String): Resource<List<TrackDataClass>> {
        return searchTrackRepository.searchTrack(expression)
    }
}