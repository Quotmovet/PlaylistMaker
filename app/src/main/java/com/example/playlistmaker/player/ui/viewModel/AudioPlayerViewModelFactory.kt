package com.example.playlistmaker.player.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.di.Creator
import com.example.playlistmaker.search.domain.model.TrackDataClass

class AudioPlayerViewModelFactory(private val trackDataClass: TrackDataClass)
    : ViewModelProvider.Factory {

        private val audioPlayerInteractor = Creator.provideAudioPlayerInteractor(trackDataClass)

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AudioPlayerViewModel(trackDataClass, audioPlayerInteractor) as T
        }
}