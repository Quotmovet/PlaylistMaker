package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.player.data.AudioPlayerRepository
import com.example.playlistmaker.player.data.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.player.domain.models.TrackModel
import com.example.playlistmaker.search.data.impl.SearchHistoryImpl
import com.example.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.SearchTrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.HistoryTrackRepositorySH
import com.example.playlistmaker.search.domain.api.SearchTrackHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.impl.SearchTrackHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.settings.data.ThemeState
import com.example.playlistmaker.settings.data.impl.SettingsThemeRepositoryImplSP
import com.example.playlistmaker.settings.data.impl.ThemeStateImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.SharingState
import com.example.playlistmaker.sharing.data.SharingStateResource
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {

    private fun getThemeStateRepository(context: Context): ThemeState {
        return ThemeStateImpl(SettingsThemeRepositoryImplSP(
            context.getSharedPreferences("settings", Context.MODE_PRIVATE)))
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

    fun provideTracksSearchInteractor(context: Context): TrackInteractor {
        return SearchTracksInteractorImpl(getTracksSearchRepository(context))
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

    fun provideAudioPlayerInteractor(trackModel: TrackModel): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(trackModel, getAudioPlayerRepository())
    }
}