package com.example.playlistmaker.search.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.search.ui.adapter.TrackListAdapter
import com.example.playlistmaker.search.ui.model.TrackState
import com.example.playlistmaker.search.ui.viewModel.SearchingViewModel
import com.example.playlistmaker.util.Constatn.KEY_FOR_PLAYER
import com.example.playlistmaker.util.mapper.TrackMapper
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment()  {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchingViewModel by viewModel()

    private val adapter = TrackListAdapter { track -> handleTrackClick(track) }
    private val historyAdapter = TrackListAdapter { track -> handleHistoryTrackClick(track) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            historyRecyclerView.adapter = historyAdapter

            inputSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.searchDebounce(s?.toString() ?: "")
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.isNullOrEmpty()) {
                        viewModel.updateHistoryList()
                    }
                }
            })

            clearButton.setOnClickListener {
                binding.inputSearch.text.clear()
                viewModel.clearSearch()
            }

            updateButton.setOnClickListener { viewModel.searchRequest(inputSearch.text.toString()) }

            clearHistoryButton.setOnClickListener { viewModel.clearHistoryList() }
        }
    }

    private fun observeViewModel() {
        viewModel.tracksState.observe(viewLifecycleOwner) { state ->
            updateTracksUI(state)
        }

        viewModel.historyList.observe(viewLifecycleOwner) { historyList ->
            updateHistoryUI(historyList)
        }

        viewModel.searchInput.observe(viewLifecycleOwner) { input ->
            updateSearchUI(input)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
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

    @SuppressLint("NotifyDataSetChanged")
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

    private fun handleTrackClick(track: TrackDataClass) {
        viewModel.addTrackToHistoryList(track)
        navigateToAudioPlayer(track)
    }

    private fun handleHistoryTrackClick(track: TrackDataClass) {
        viewModel.transferTrackToTop(track)
        navigateToAudioPlayer(track)
    }

    private fun navigateToAudioPlayer(track: TrackDataClass) {
        val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
        intent.putExtra(KEY_FOR_PLAYER, TrackMapper.mapTrackDomainToUi(track))
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveHistoryList()
    }
}