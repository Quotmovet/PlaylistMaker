package com.example.playlistmaker.di

import com.example.playlistmaker.main.viewmodel.MainViewModel
import com.example.playlistmaker.media.viewmodel.FavoritesViewModel
import com.example.playlistmaker.media.viewmodel.PlayListViewModel
import com.example.playlistmaker.player.ui.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.search.ui.viewModel.SearchingViewModel
import com.example.playlistmaker.settings.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel()
    }

    viewModel {
        FavoritesViewModel()
    }
    viewModel {
        PlayListViewModel()
    }

    viewModel { (track: TrackDataClass) ->
        AudioPlayerViewModel(track, get { parametersOf(track) })
    }

    viewModel {
        SearchingViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
}