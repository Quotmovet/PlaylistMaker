package com.example.playlistmaker.search.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.search.ui.adapter.TrackListAdapter
import com.example.playlistmaker.search.ui.model.TrackState
import com.example.playlistmaker.util.Constatn.KEY_FOR_PLAYER
import com.example.playlistmaker.search.ui.viewModel.SearchViewModelFactory
import com.example.playlistmaker.search.ui.viewModel.SearchingViewModel
import com.example.playlistmaker.util.mapper.TrackMapper

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchingViewModel

    private val adapter = TrackListAdapter { track -> handleTrackClick(track) }
    private val historyAdapter = TrackListAdapter { track -> handleHistoryTrackClick(track) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,
            SearchViewModelFactory(this))[SearchingViewModel::class.java]

        setupViews()
        observeViewModel()
    }

    // Обработчики нажатий
    private fun setupViews() {
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            recyclerView.adapter = adapter

            historyRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            historyRecyclerView.adapter = historyAdapter

            // Фокус на поле поиска при открытии активности
            inputSearch.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && binding.inputSearch.text.isNullOrEmpty()) {
                    viewModel.updateHistoryList()
                }
            }

            // Наблюдатель за изменением текста в поле поиска
            inputSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.searchDebounce(s?.toString() ?: "")
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            // Вернуться назад
            mainBackButton.setNavigationOnClickListener {
                finish()
            }

            // Очистить поле поиска
            clearButton.setOnClickListener {
                binding.inputSearch.text.clear()
                viewModel.clearSearch()
            }

            // Обновить запрос
            updateButton.setOnClickListener { viewModel.searchRequest(inputSearch.text.toString()) }

            // Очистить историю
            clearHistoryButton.setOnClickListener { viewModel.clearHistoryList() }
        }
    }

    // Наблюдатель за ViewModel
    private fun observeViewModel() {
        viewModel.tracksState.observe(this) { state ->
            updateTracksUI(state)
        }

        viewModel.historyList.observe(this) { historyList ->
            updateHistoryUI(historyList)
        }

        viewModel.searchInput.observe(this) { input ->
            updateSearchUI(input)
        }
    }

    // Обновление UI в зависимости от состояния ViewModel и событий
    private fun updateTracksUI(state: TrackState) {
        with(binding) {
            searchProgressBar.isVisible = state.isLoading
            searchErrorNothingFound.isVisible = state.tracks.isEmpty() && !state.isLoading && state.isFailed == true
            searchErrorNetwork.isVisible = state.isFailed == false

            if (state.tracks.isNotEmpty()) {
                adapter.tracks = state.tracks as ArrayList<TrackDataClass>
                adapter.notifyDataSetChanged()
                recyclerView.isVisible = true
            } else {
                recyclerView.isVisible = false
            }
        }
    }

    // Обновление истории UI в зависимости от состояния ViewModel и событий
    private fun updateHistoryUI(historyList: ArrayList<TrackDataClass>) {
        with(binding) {
            val hasHistory = historyList.isNotEmpty()
            val showHistory = hasHistory && binding.inputSearch.text.isEmpty()

            historyLayout.isVisible = showHistory
            youWereLookingFor.isVisible = showHistory
            historyRecyclerView.isVisible = showHistory
            clearHistoryButton.isVisible = showHistory

            if (showHistory) {
                historyAdapter.tracks = historyList
                historyAdapter.notifyDataSetChanged()
            }
        }
    }

    // Обновление UI в зависимости от текста в поле поиска и состояния ViewModel и событий
    private fun updateSearchUI(input: String) {
        with(binding) {
            clearButton.isVisible = input.isNotEmpty()

            if (input.isEmpty()) {
                recyclerView.isVisible = false
                searchErrorNothingFound.isVisible = false
                searchErrorNetwork.isVisible = false
                viewModel.updateHistoryList()
            } else {
                historyLayout.isVisible = false
            }
        }
    }

    // Обработчики нажатий на треки и истории
    private fun handleTrackClick(track: TrackDataClass) {
        viewModel.addTrackToHistoryList(track)
        navigateToAudioPlayer(track)
    }

    // Обработчик нажатий на треки истории (перемещение вверх)
    private fun handleHistoryTrackClick(track: TrackDataClass) {
        viewModel.transferTrackToTop(track)
        navigateToAudioPlayer(track)
    }

    // Переход к активности воспроизведения музыки (с передачей трека)
    private fun navigateToAudioPlayer(track: TrackDataClass) {
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(KEY_FOR_PLAYER, TrackMapper.mapTrackDomainToUi(track))
        startActivity(intent)
    }

    // Сохранение истории при остановке активности
    override fun onStop() {
        super.onStop()
        viewModel.saveHistoryList()
    }
}