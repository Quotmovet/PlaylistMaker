package com.example.playlistmaker.di

import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.SearchTrackRepositoryImpl
import com.example.playlistmaker.domain.api.SearchTrackRepository
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.impl.SearchTracksInteractorImpl

object Creator {
    private fun getSearchTrackRepository(): SearchTrackRepository {
        return SearchTrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSearchTrackInteractor(): TrackInteractor {
        return SearchTracksInteractorImpl(getSearchTrackRepository())
    }
}