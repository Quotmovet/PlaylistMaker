package com.example.playlistmaker.media.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.interactor.FavoriteTracksInteractor
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private val favoritesLiveData = MutableLiveData<List<TrackDataClass>>()

    // Получение избранных треков
    fun getFavorites () {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteTracksInteractor
                .getFavoriteTracks()
                .collect { favoritesLiveData.postValue(updatFavoritesTracks(it)) }
        }
    }

    // Получение LiveData
    fun getFavoritesTracksLiveData(): LiveData<List<TrackDataClass>> = favoritesLiveData

    // Обновление статуса избранности у треков
    private fun updatFavoritesTracks (tracks: List<TrackDataClass>): List<TrackDataClass> {
        tracks.forEach { track -> track.isFavorite = true }
        return tracks
    }
}