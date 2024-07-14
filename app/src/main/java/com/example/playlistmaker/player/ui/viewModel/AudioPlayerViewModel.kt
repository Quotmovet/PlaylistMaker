package com.example.playlistmaker.player.ui.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.mapper.TrackMapper
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val trackDataClass: TrackDataClass,
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    companion object {
        private const val UPDATE_INTERVAL = 1000L
    }

    // Состояния плеера
    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    // Информация о треке
    private val _trackInfo = MutableLiveData<TrackDataClass>()
    val trackInfo: LiveData<TrackDataClass> = _trackInfo

    // Текущая позиция
    private val _currentPosition = MutableLiveData<String>()

    // Завершен ли трек
    private val _isTrackCompleted = MutableLiveData<Boolean>()

    // Текущая позиция и проверка на завершенности трека (объединение)
    private val _isCompleteAndCurrentPositionState = MediatorLiveData<Pair<Boolean, String>>()
    val isCompleteAndCurrentPositionState: LiveData<Pair<Boolean, String>> = _isCompleteAndCurrentPositionState

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

        _isCompleteAndCurrentPositionState.addSource(_isTrackCompleted) { isCompleted ->
            _isCompleteAndCurrentPositionState.value =
                Pair(isCompleted, _currentPosition.value ?: "00:00")
        }
        _isCompleteAndCurrentPositionState.addSource(_currentPosition) { position ->
            _isCompleteAndCurrentPositionState.value =
                Pair(_isTrackCompleted.value ?: false, position)
        }
    }

    // Подготовка плеера
    private fun prepareAudioPlayer() {
        audioPlayerInteractor.prepare(
            callbackPrepare = {
                _playerState.postValue(PlayerState.PREPARED)
                _currentPosition.postValue("00:00")
            },
            callbackComplete = {
                _playerState.postValue(PlayerState.COMPLETED)
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

    // Освобождение ресурсов
    override fun onCleared() {
        super.onCleared()
        stopPositionUpdates()
        audioPlayerInteractor.release()
    }
}