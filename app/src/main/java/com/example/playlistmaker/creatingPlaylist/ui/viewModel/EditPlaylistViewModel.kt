package com.example.playlistmaker.creatingPlaylist.ui.viewModel

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creatingPlaylist.domain.interactor.CreatingPlaylistInteractor
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.creatingPlaylist.ui.fragment.CreatingPlaylistFragment
import com.example.playlistmaker.creatingPlaylist.ui.state.StatePlaylistAdded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    interactor: CreatingPlaylistInteractor
) : CreatingPlaylistViewModel(interactor) {

    private val _isPlaylistUpdated = MutableStateFlow(StatePlaylistAdded.NOTHING)
    val isPlaylistUpdated: StateFlow<StatePlaylistAdded> = _isPlaylistUpdated.asStateFlow()

    override fun isFilled() {}

    fun savePlaylist(
        playlist: PlaylistDataClass
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updatedUri = if (playListUri.isNotEmpty()) {
                    interactor.saveImageToPrivateStorage(playListUri)
                } else {
                    playlist.uriOfImage
                }

                val updatedPlaylist = playlist.copy(
                    playlistName = playListName,
                    descriptionPlaylist = playListDescription.ifEmpty { null },
                    uriOfImage = updatedUri
                )

                interactor.updatePlaylist(updatedPlaylist).collect { result ->
                    if (result > 0) {
                        _isPlaylistUpdated.value = StatePlaylistAdded.SUCCESS
                    } else {
                        _isPlaylistUpdated.value = StatePlaylistAdded.ERROR
                    }
                }
            } catch (e: Exception) {
                _isPlaylistUpdated.value = StatePlaylistAdded.ERROR
            }
        }
        CreatingPlaylistFragment.isCreatePlaylistFragmentFilled = false
    }
}