package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository.SearchTrackRepositoryImpl
import com.example.playlistmaker.search.domain.repository.HistoryTrackRepositorySH
import com.example.playlistmaker.search.domain.repository.SearchTrackRepository
import com.example.playlistmaker.settings.data.repository.SettingsRepositoryImplSP
import com.example.playlistmaker.settings.domain.repository.SettingsRepository
import com.example.playlistmaker.sharing.data.datasource.SharingStateRepositoryImpl
import com.example.playlistmaker.sharing.domain.repository.SharingStateRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl()
    }

    single<SearchTrackRepository> {
        SearchTrackRepositoryImpl(get())
    }
    single<HistoryTrackRepositorySH> {
        SearchHistoryRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImplSP(get())
    }
    single<SharingStateRepository> {
        SharingStateRepositoryImpl(androidContext())
    }
}