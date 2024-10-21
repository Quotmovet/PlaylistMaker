package com.example.playlistmaker.media.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.media.domain.interactor.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel (
    private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private var playlistsLiveData = MutableLiveData(listOf<PlaylistDataClass>())

    fun getPlaylists () {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor
                .getPlaylists()
                .collect { playlistsLiveData.postValue(it) }
        }
    }

    fun getPlaylistsLiveData(): LiveData<List<PlaylistDataClass>> = playlistsLiveData
}