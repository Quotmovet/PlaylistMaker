package com.example.playlistmaker.playlist.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creatingPlaylist.domain.interactor.CreatingPlaylistInteractor
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.media.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.playlist.domain.PlaylistPageInteractor
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistPageViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistPageInteractor: PlaylistPageInteractor,
    private val createPlaylistInteractor: CreatingPlaylistInteractor
) : ViewModel() {

    private var playlist: PlaylistDataClass? = null
    private var playlistPageLiveData = MutableLiveData<PlaylistDataClass?>(playlist)

    fun getPlaylistPageLiveData(): LiveData<PlaylistDataClass?> = playlistPageLiveData

    fun getPlaylistById (playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistPageInteractor
                .getPlaylistById(playlistId)
                .collect { playlist ->
                    playlistPageLiveData.postValue(playlist)
                    playlistPageInteractor
                        .getPlaylistTrackList(playlist.tracksListId)
                        .collect { playlistTracksLiveData.postValue(it) }
                }
        }
    }

    fun getPlaylistTrackList(playlistIdList: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistPageInteractor
                .getPlaylistTrackList(playlistIdList)
                .collect { tracks ->
                    Log.d("PlaylistPageViewModel", "Received tracks: ${tracks.size}")
                    tracks.forEachIndexed { index, track ->
                        Log.d("PlaylistPageViewModel", "Track $index: ${track.trackName}")
                    }
                    playlistTracksLiveData.postValue(tracks)
                }
        }
    }

    private var playlistTracksLiveData = MutableLiveData(listOf<TrackDataClass>())

    fun getPlaylistTracksLiveData(): LiveData<List<TrackDataClass>> = playlistTracksLiveData

    fun updatePlaylist(playlist: PlaylistDataClass, track: TrackDataClass) {
        val trackId = track.trackId
        playlist.tracksListId.remove(trackId)

        viewModelScope.launch(Dispatchers.IO) {
            createPlaylistInteractor
                .updatePlaylist(playlist)
                .collect {}
        }
    }

    fun deleteTrack (trackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor
                .getPlaylists()
                .collect {
                    if (trackCheckForDeletion(trackId, it)) {
                        playlistInteractor.deleteTrackById(trackId)
                    }
                }
        }
    }

    // Проверка на наличие трека в плейлистах
    private fun trackCheckForDeletion (trackId: Int, list: List<PlaylistDataClass>): Boolean {
        for (playlist: PlaylistDataClass in list) {
            if (!playlist.tracksListId.none { it == trackId }) return false
        }
        return true
    }


    fun deletePlaylistById(playlistId: Int, tracksId: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistPageInteractor.deletePlaylistById(playlistId)
            playlistInteractor
                .getPlaylists()
                .collect { playlists ->
                    val masterPlaylist = mutableSetOf<Int>()

                    for (playlist: PlaylistDataClass in playlists) {
                        masterPlaylist.addAll(playlist.tracksListId)
                    }

                    for (trackId: Int in tracksId) {
                        if (masterPlaylist.none { it == trackId }) {
                            playlistInteractor.deleteTrackById(trackId)
                        }
                    }
                }
        }
    }
}