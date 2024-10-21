package com.example.playlistmaker.player.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creatingPlaylist.domain.interactor.CreatingPlaylistInteractor
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val trackDataClass: TrackDataClass,
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val creatingPlaylistInteractor: CreatingPlaylistInteractor,
) : ViewModel() {

    companion object {
        private const val UPDATE_INTERVAL = 300L
    }

    private var timerJob: Job? = null

    // Состояние плеера
    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun getTrackStateAudioPlayerLiveData(): LiveData<PlayerState> = _playerState

    // Кнопка добавления в избранное
    private val stateFavoritesButtonLiveData = MutableLiveData(trackDataClass.isFavorite)
    fun getTrackIsFavoriteLiveData(): LiveData<Boolean> = stateFavoritesButtonLiveData

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _currentTime = MutableLiveData<String>()

    private val _trackInfo = MutableLiveData<TrackDataClass>()
    val trackInfo: LiveData<TrackDataClass> = _trackInfo

    private val _addTrackStatus = MutableLiveData<AddTrackStatus>()
    val addTrackStatus: LiveData<AddTrackStatus> = _addTrackStatus

    init {
        initMediaPlayer()
        updateTrackInfo()
        initFavoriteState()
    }

    // Подготовка состояния избранного
    private fun initFavoriteState() {
        viewModelScope.launch {
            val isFavorite = audioPlayerInteractor.isTrackFavorite(trackDataClass.trackId)
            trackDataClass.isFavorite = isFavorite
            stateFavoritesButtonLiveData.postValue(isFavorite)
        }
    }

    // Подготовка плеера
    private fun initMediaPlayer() {
        audioPlayerInteractor.setDataSource(trackDataClass.previewUrl)
        audioPlayerInteractor.prepareAsync()
        audioPlayerInteractor.setOnPreparedListener {
            _playerState.postValue(PlayerState.Prepared())
        }
        audioPlayerInteractor.setOnCompletionListener {
            _playerState.postValue(PlayerState.Prepared())
            _currentTime.postValue("00:00")
            _currentTime.postValue(getCurrentPlayerPosition())
            timerJob?.cancel()
        }
    }

    // Контроллер плеера
    fun onPlayButtonClicked() {
        when (_playerState.value) {
            is PlayerState.Playing -> pause()
            is PlayerState.Prepared, is PlayerState.Paused -> start()
            else -> { /* Не делаем ничего */ }
        }
    }

    // Контроллер избранного
    fun onFavoriteClicked() {
        viewModelScope.launch {
            val newFavoriteState = !trackDataClass.isFavorite
            audioPlayerInteractor.onFavoriteClick(trackDataClass)
            trackDataClass.isFavorite = newFavoriteState
            _isFavorite.postValue(newFavoriteState)
            stateFavoritesButtonLiveData.postValue(newFavoriteState)
        }
    }

    private fun start() {
        audioPlayerInteractor.start()
        _playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    fun pause() {
        audioPlayerInteractor.pause()
        timerJob?.cancel()
        _playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    // Сброс состояния и освобождение ресурсов
    private fun release() {
        audioPlayerInteractor.stop()
        audioPlayerInteractor.release()
        _playerState.value = PlayerState.Default()
    }

    // Обновление текущего времени
    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (audioPlayerInteractor.isPlaying()) {
                delay(UPDATE_INTERVAL)
                _playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(audioPlayerInteractor.getCurrentPosition()) ?: "00:00"
    }

    // Освобождение ресурсов
    override fun onCleared() {
        super.onCleared()
        release()
        timerJob?.cancel()
    }

    private fun updateTrackInfo() {
        val updatedTrackDataClass = trackDataClass.copy(
            artworkUrl1100 = trackDataClass.artworkUrl1100.replaceAfterLast('/', "512x512bb.jpg"),
            releaseDate = trackDataClass.releaseDate.split("-", limit = 2)[0],
            trackTimeMillis = TimeUtils.formatTime(trackDataClass.trackTimeMillis.toLong())
        )
        _trackInfo.postValue(updatedTrackDataClass)
    }

    fun addTrackToPlaylist(playlist: PlaylistDataClass, track: TrackDataClass) {
        viewModelScope.launch {
            if (playlist.tracksListId.contains(track.trackId)) {
                _addTrackStatus.value = AddTrackStatus.AlreadyExists(playlist.playlistName)
            } else {
                try {
                    withContext(Dispatchers.IO) {
                        creatingPlaylistInteractor.addTrackInPlaylist(track)
                    }

                    playlist.tracksListId.add(track.trackId)
                    playlist.trackCount++

                    withContext(Dispatchers.IO) {
                        creatingPlaylistInteractor.updatePlaylist(playlist).collect { result ->
                            withContext(Dispatchers.Main) {
                                if (result > 0) {
                                    _addTrackStatus.value = AddTrackStatus.Success(playlist.playlistName)
                                } else {
                                    _addTrackStatus.value = AddTrackStatus.Error
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AudioPlayerViewModel", "Error adding track to playlist: ${e.message}")
                    _addTrackStatus.value = AddTrackStatus.Error
                }
            }
        }
    }
}

sealed class AddTrackStatus {
    data class Success(val playlistName: String) : AddTrackStatus()
    data class AlreadyExists(val playlistName: String) : AddTrackStatus()
    data object Error : AddTrackStatus()
}