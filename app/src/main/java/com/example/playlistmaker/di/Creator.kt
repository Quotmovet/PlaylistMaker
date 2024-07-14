package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.interactor.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.search.data.storage.SearchHistoryImpl
import com.example.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository.SearchTrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.repository.HistoryTrackRepositorySH
import com.example.playlistmaker.search.domain.interactor.SearchTrackHistoryInteractor
import com.example.playlistmaker.search.domain.repository.SearchTrackRepository
import com.example.playlistmaker.search.domain.interactor.SearchTrackInteractor
import com.example.playlistmaker.search.domain.interactor.impl.SearchTrackHistoryInteractorImpl
import com.example.playlistmaker.search.domain.interactor.impl.SearchTrackInteractorImpl
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.settings.domain.repository.ThemeState
import com.example.playlistmaker.settings.data.repository.SettingsThemeRepositoryImplSP
import com.example.playlistmaker.settings.data.repository.ThemeStateImpl
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.settings.domain.interactor.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.repository.SharingState
import com.example.playlistmaker.sharing.data.datasource.SharingStateResource
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.example.playlistmaker.sharing.domain.interactor.impl.SharingInteractorImpl

object Creator {

    private fun getThemeStateRepository(context: Context): ThemeState {
        return ThemeStateImpl(
            SettingsThemeRepositoryImplSP(
            context.getSharedPreferences("settings", Context.MODE_PRIVATE))
        )
    }

    fun provideThemeStateInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getThemeStateRepository(context))
    }

    private fun getStringStorageRepository(context: Context): SharingState {
        return SharingStateResource(context)
    }

    fun provideStringStorageInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getStringStorageRepository(context))
    }

    private fun getTracksSearchRepository(context: Context): SearchTrackRepository {
        return SearchTrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTracksSearchInteractor(context: Context): SearchTrackInteractor {
        return SearchTrackInteractorImpl(getTracksSearchRepository(context))
    }

    private fun getHistoryTrackRepositorySH(context: Context): HistoryTrackRepositorySH {
        return SearchHistoryRepositoryImpl(SearchHistoryImpl(context))
    }

    fun provideTrackHistoryInteractor(context: Context): SearchTrackHistoryInteractor {
        return SearchTrackHistoryInteractorImpl(getHistoryTrackRepositorySH(context))
    }

    private fun getAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    fun provideAudioPlayerInteractor(trackDataClass: TrackDataClass): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(trackDataClass, getAudioPlayerRepository())
    }
}