package com.example.playlistmaker.player.ui.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.models.TrackModel
import com.example.playlistmaker.player.ui.activity.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val trackModel: TrackModel,
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    companion object {
        private const val UPDATE_INTERVAL = 1000L
    }

    // Состояния плеера
    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    // Текущая позиция
    private val _currentPosition = MutableLiveData<String>()
    val currentPosition: LiveData<String> = _currentPosition

    // Информация о треке
    private val _trackInfo = MutableLiveData<TrackModel>()
    val trackInfo: LiveData<TrackModel> = _trackInfo

    // Завершен ли трек
    private val _isTrackCompleted = MutableLiveData<Boolean>()
    val isTrackCompleted: LiveData<Boolean> = _isTrackCompleted

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    // Запуск обновления позиции
    private val updatePositionRunnable = object : Runnable {
        override fun run() {
            updatePosition()
            mainThreadHandler.postDelayed(this, UPDATE_INTERVAL)
        }
    }

    init {
        prepareAudioPlayer()
        updateTrackInfo()
    }

    // Подготовка плеера
    private fun prepareAudioPlayer() {
        audioPlayerInteractor.prepare(
            callbackPrepare = {
                _playerState.postValue(PlayerState.PREPARED)
                _currentPosition.postValue("00:00")
            },
            callbackComplete = {
                _playerState.postValue(PlayerState.STOPPED)
                stopPositionUpdates()
                _isTrackCompleted.postValue(true)
                _currentPosition.postValue("00:00")
            }
        )
    }

    // Контроллер плеера
    fun playbackControl() {
        when (audioPlayerInteractor.getState()) {
            PlayerState.PREPARED, PlayerState.PAUSED, PlayerState.COMPLETED -> play()
            PlayerState.STARTED -> pause()
            else -> { /* Не делаем ничего */ }
        }
    }

    private fun play() {
        audioPlayerInteractor.play()
        _playerState.postValue(PlayerState.STARTED)
        startPositionUpdates()
        _isTrackCompleted.postValue(false)
    }

    fun pause() {
        audioPlayerInteractor.pause()
        _playerState.postValue(PlayerState.PAUSED)
        stopPositionUpdates()
    }

    // Обновление позиций
    private fun updatePosition() {
        if (audioPlayerInteractor.getState() == PlayerState.STARTED) {
            val position = audioPlayerInteractor.getCurrentPosition()
            _currentPosition.postValue(formatTime(position.toLong()))
        }
    }

    private fun startPositionUpdates() {
        mainThreadHandler.post(updatePositionRunnable)
    }

    private fun stopPositionUpdates() {
        mainThreadHandler.removeCallbacks(updatePositionRunnable)
    }

    // Обновление информации о треке
    private fun updateTrackInfo() {
        val updatedTrackModel = trackModel.copy(
            artworkUrl1100 = trackModel.artworkUrl1100.replaceAfterLast('/', "512x512bb.jpg"),
            releaseDate = trackModel.releaseDate.split("-", limit = 2)[0],
            trackTimeMillis = formatTime(trackModel.trackTimeMillis.toLong())
        )
        _trackInfo.postValue(updatedTrackModel)
    }

    // Форматирование времени
    private fun formatTime(timeMillis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }

    // Освобождение ресурсов
    override fun onCleared() {
        super.onCleared()
        stopPositionUpdates()
        audioPlayerInteractor.release()
    }
}