package com.example.playlistmaker.search.ui.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.SearchTrackHistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.search.ui.model.TrackState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchingViewModel(
    private val trackInteractor: TrackInteractor,
    private val searchTrackHistoryInteractor: SearchTrackHistoryInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DELAY = 1500L
    }

    private val _tracksState = MutableLiveData<TrackState>()
    val tracksState: LiveData<TrackState> = _tracksState

    private val _searchInput = MutableLiveData<String>()
    val searchInput: LiveData<String> = _searchInput

    private val _historyList = MutableLiveData<ArrayList<TrackDataClass>>()
    val historyList: LiveData<ArrayList<TrackDataClass>> = _historyList

    private var lastSearchText: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { lastSearchText?.let { searchRequest(it) } }

    init {
        updateHistoryList()
    }

    // Дебаунсер
    fun searchDebounce(changedText: String) {
        _searchInput.value = changedText
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DELAY)
    }

    // Запрос
    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            _tracksState.value = TrackState(emptyList(), isLoading = true, isFailed = null)

            viewModelScope.launch {
                val result = withContext(Dispatchers.IO) {
                    trackInteractor.searchTrack(newSearchText)
                }
                _tracksState.value = TrackState(
                    tracks = result.data ?: emptyList(),
                    isLoading = false,
                    isFailed = result.isFailed
                )
                updateHistoryList()
            }
        } else {
            clearSearch()
        }
    }

    // Очистка поиска
    fun clearSearch() {
        _tracksState.value = TrackState(emptyList(), isLoading = false, isFailed = null)
        updateHistoryList()
    }

    // Очистка истории поиска и восстановление из SharedPreferences
    fun clearHistoryList() {
        searchTrackHistoryInteractor.clearHistoryList()
        updateHistoryList()
    }

    // Добавление трека в историю поиска и сохранение в SharedPreferences
    fun addTrackToHistoryList(track: TrackDataClass) {
        searchTrackHistoryInteractor.addTrackToHistoryList(track)
        updateHistoryList()
    }

    // Перемещение трека в истории поиска вверх и сохранение в SharedPreferences
    fun transferTrackToTop(track: TrackDataClass) {
        searchTrackHistoryInteractor.transferToTop(track)
        updateHistoryList()
    }

    // Сохранение истории поиска в SharedPreferences
    fun saveHistoryList() {
        searchTrackHistoryInteractor.saveHistoryList()
    }

    // Обновление истории
    fun updateHistoryList() {
        viewModelScope.launch(Dispatchers.Main) {
            val history = withContext(Dispatchers.IO) {
                searchTrackHistoryInteractor.getHistoryList()
            }
            _historyList.value = history
        }
    }

    // Очистка ViewModel при остановке активности
    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }
}