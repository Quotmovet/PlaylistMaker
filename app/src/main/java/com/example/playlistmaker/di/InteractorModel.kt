package com.example.playlistmaker.di

import com.example.playlistmaker.creatingPlaylist.domain.interactor.CreatingPlaylistInteractor
import com.example.playlistmaker.creatingPlaylist.domain.interactor.impl.CreatingPlaylistInteractorImpl
import com.example.playlistmaker.media.domain.interactor.impl.FavoritesTracksInteractorImpl
import com.example.playlistmaker.media.domain.interactor.FavoriteTracksInteractor
import com.example.playlistmaker.media.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.media.domain.interactor.impl.PlaylistInteractorImpl
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.interactor.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.playlist.domain.PlaylistPageInteractor
import com.example.playlistmaker.playlist.domain.impl.PlaylistPageInteractorImpl
import com.example.playlistmaker.search.domain.interactor.SearchTrackHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.SearchTrackInteractor
import com.example.playlistmaker.search.domain.interactor.impl.SearchTrackHistoryInteractorImpl
import com.example.playlistmaker.search.domain.interactor.impl.SearchTrackInteractorImpl
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.settings.domain.interactor.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.example.playlistmaker.sharing.domain.interactor.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get(), get(), get())
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

    single<FavoriteTracksInteractor> {
        FavoritesTracksInteractorImpl(get())
    }

    single<CreatingPlaylistInteractor> {
        CreatingPlaylistInteractorImpl(get(), get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    single<PlaylistPageInteractor> {
        PlaylistPageInteractorImpl(get())
    }
}