package com.example.playlistmaker.di

import com.example.playlistmaker.creatingPlaylist.ui.viewModel.CreatingPlaylistViewModel
import com.example.playlistmaker.creatingPlaylist.ui.viewModel.EditPlaylistViewModel
import com.example.playlistmaker.media.ui.viewmodel.FavoritesViewModel
import com.example.playlistmaker.media.ui.viewmodel.PlaylistViewModel
import com.example.playlistmaker.player.ui.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.playlist.ui.viewmodel.PlaylistPageViewModel
import com.example.playlistmaker.root.viewModel.RootViewModel
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.search.ui.viewModel.SearchingViewModel
import com.example.playlistmaker.settings.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        RootViewModel(get())
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel {
        CreatingPlaylistViewModel(get())
    }

    viewModel {
        EditPlaylistViewModel(get())
    }

    viewModel { (track: TrackDataClass) ->
        AudioPlayerViewModel(track, get { parametersOf(track) }, get())
    }

    viewModel {
        SearchingViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel{
        PlaylistPageViewModel(get(), get(), get())
    }
}