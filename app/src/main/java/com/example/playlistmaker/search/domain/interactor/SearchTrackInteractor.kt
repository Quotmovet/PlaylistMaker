package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.search.domain.model.TrackDataClass

interface SearchTrackInteractor {
    fun searchTrack(expression: String): Resource<List<TrackDataClass>>
}