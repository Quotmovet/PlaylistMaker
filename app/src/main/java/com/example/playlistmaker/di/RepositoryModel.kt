package com.example.playlistmaker.di

import com.example.playlistmaker.creatingPlaylist.data.impl.CreatingPlaylistRepositoryImpl
import com.example.playlistmaker.creatingPlaylist.domain.repository.CreatingPlaylistRepository
import com.example.playlistmaker.media.data.db.converter.TrackDbConverter
import com.example.playlistmaker.media.data.impl.FavoritesRepositoryImpl
import com.example.playlistmaker.media.data.impl.PlaylistRepositoryImpl
import com.example.playlistmaker.media.domain.db.FavoritesRepository
import com.example.playlistmaker.media.domain.db.PlaylistRepository
import com.example.playlistmaker.player.data.impl.AudioPlayerRepositoryImpl
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

    factory{ TrackDbConverter() }

    single<SearchTrackRepository> {
        SearchTrackRepositoryImpl(get(), get())
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

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    single<CreatingPlaylistRepository> {
        CreatingPlaylistRepositoryImpl(androidContext(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl( get(), get())
    }
}