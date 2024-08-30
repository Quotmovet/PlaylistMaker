package com.example.playlistmaker.player.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.mapper.TrackMapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val trackDataClass: TrackDataClass,
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    companion object {
        private const val UPDATE_INTERVAL = 300L
    }

    private var timerJob: Job? = null

    // Состояние плеера
    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun getTrackStateAudioPlayerLiveData(): LiveData<PlayerState> = _playerState

    // Текущее время
    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String> = _currentTime

    // Информация о треке
    private val _trackInfo = MutableLiveData<TrackDataClass>()
    val trackInfo: LiveData<TrackDataClass> = _trackInfo

    init {
        initMediaPlayer()
        Log.d("AudioPlayerViewModel", "ViewModel initialized with track: $trackDataClass")
        updateTrackInfo()
        Log.d("AudioPlayerViewModel", "ViewModel updated track: $trackDataClass")
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

    // Начало проигрывания
    private fun start() {
        audioPlayerInteractor.start()
        _playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    // Пауза
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

    // Текущее время
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

    // Обновление информации о треке
    private fun updateTrackInfo() {
        val updatedtrackDataClass = TrackMapper.mapTrackDomainToUi(trackDataClass).copy(
            artworkUrl1100 = trackDataClass.artworkUrl1100.replaceAfterLast('/', "512x512bb.jpg"),
            releaseDate = trackDataClass.releaseDate.split("-", limit = 2)[0],
            trackTimeMillis = formatTime(trackDataClass.trackTimeMillis.toLong())
        )
        _trackInfo.postValue(updatedtrackDataClass)
    }

    // Форматирование времени
    private fun formatTime(timeMillis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }
}