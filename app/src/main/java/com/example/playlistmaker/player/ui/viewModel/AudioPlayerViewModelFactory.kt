package com.example.playlistmaker.player.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.di.Creator
import com.example.playlistmaker.player.domain.models.TrackModel

class AudioPlayerViewModelFactory(private val trackModel: TrackModel)
    : ViewModelProvider.Factory {

        private val audioPlayerInteractor = Creator.provideAudioPlayerInteractor(trackModel)

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AudioPlayerViewModel(trackModel, audioPlayerInteractor) as T
        }
}