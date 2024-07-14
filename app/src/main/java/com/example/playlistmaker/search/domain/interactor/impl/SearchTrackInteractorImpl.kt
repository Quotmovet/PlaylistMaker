package com.example.playlistmaker.search.domain.interactor.impl

import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.search.domain.interactor.SearchTrackInteractor
import com.example.playlistmaker.search.domain.repository.SearchTrackRepository
import com.example.playlistmaker.search.domain.model.TrackDataClass

class SearchTrackInteractorImpl(
    private val searchTrackRepository: SearchTrackRepository
) : SearchTrackInteractor {

        override fun searchTrack(expression: String): Resource<List<TrackDataClass>> {
        return searchTrackRepository.searchTrack(expression)
    }
}