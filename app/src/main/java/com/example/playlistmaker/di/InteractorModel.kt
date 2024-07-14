package com.example.playlistmaker.di

import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.interactor.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.search.domain.interactor.SearchTrackHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.SearchTrackInteractor
import com.example.playlistmaker.search.domain.interactor.impl.SearchTrackHistoryInteractorImpl
import com.example.playlistmaker.search.domain.interactor.impl.SearchTrackInteractorImpl
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.settings.domain.interactor.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.example.playlistmaker.sharing.domain.interactor.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<AudioPlayerInteractor> { (track: TrackDataClass) ->
        AudioPlayerInteractorImpl(track, get())
    }

    single<SearchTrackInteractor> {
        SearchTrackInteractorImpl(get())
    }
    single<SearchTrackHistoryInteractor> {
        SearchTrackHistoryInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
}