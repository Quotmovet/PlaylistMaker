package com.example.playlistmaker.creatingPlaylist.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creatingPlaylist.domain.interactor.CreatingPlaylistInteractor
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.creatingPlaylist.ui.state.StatePlaylistAdded
import com.example.playlistmaker.creatingPlaylist.ui.fragment.CreatingPlaylistFragment
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreatingPlaylistViewModel(
    private val interactor: CreatingPlaylistInteractor
) : ViewModel() {

    private var playListName: String = ""
    private var playListDescription: String = ""
    private var playListUri: String = ""

    private val _isPlaylistAdded = MutableStateFlow(StatePlaylistAdded.NOTHING)
    val isPlaylistAdded: StateFlow<StatePlaylistAdded> = _isPlaylistAdded.asStateFlow()

    init {
        CreatingPlaylistFragment.isCreatePlaylistFragmentFilled = false
    }

    fun addNewPlaylist(track: TrackDataClass? = null) {
        if (playListName.isEmpty()) {
            _isPlaylistAdded.value = StatePlaylistAdded.ERROR
            return
        }

        _isPlaylistAdded.value = StatePlaylistAdded.LOADING

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUri = if (playListUri.isNotEmpty()) {
                    interactor.saveImageToPrivateStorage(playListUri)
                } else null

                val trackIds = if (track != null) mutableListOf(track.trackId) else mutableListOf()

                val playList = PlaylistDataClass(
                    0,
                    playListName,
                    playListDescription.ifEmpty { null },
                    imageUri,
                    trackIds,
                    trackIds.size
                )

                interactor.addNewPlaylist(playList).collect { id ->
                    if (id > 0) {
                        if (track != null) {
                            interactor.addTrackInPlaylist(track)
                        }
                        _isPlaylistAdded.value = StatePlaylistAdded.SUCCESS
                    } else {
                        _isPlaylistAdded.value = StatePlaylistAdded.ERROR
                    }
                }
            } catch (e: Exception) {
                Log.e("CreatingPlaylistViewModel", "Error adding playlist", e)
                _isPlaylistAdded.value = StatePlaylistAdded.ERROR
            }
        }
        CreatingPlaylistFragment.isCreatePlaylistFragmentFilled = false
    }

    fun setName(name: String) {
        playListName = name
        updateFilledStatus()
    }

    fun setDescription(description: String) {
        playListDescription = description
        updateFilledStatus()
    }

    fun setUri(uri: String?) {
        playListUri = uri ?: ""
        updateFilledStatus()
    }

    private fun updateFilledStatus() {
        CreatingPlaylistFragment.isCreatePlaylistFragmentFilled =
            playListName.isNotEmpty() || playListDescription.isNotEmpty() || playListUri.isNotEmpty()
    }

    fun resetState() {
        _isPlaylistAdded.value = StatePlaylistAdded.NOTHING
    }
}