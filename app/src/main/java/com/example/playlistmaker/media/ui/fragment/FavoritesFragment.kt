package com.example.playlistmaker.media.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.media.ui.viewmodel.FavoritesViewModel
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.player.ui.activity.isChangedFavorites
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.search.ui.adapter.TrackListAdapter
import com.example.playlistmaker.util.Constatn.KEY_FOR_PLAYER
import com.example.playlistmaker.util.mapper.TrackMapper
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private lateinit var binding: FragmentFavoritesBinding

    private val viewModel: FavoritesViewModel by viewModel()

    private val adapter = TrackListAdapter { track -> navigateToAudioPlayer(track) }

    private val tracks = ArrayList<TrackDataClass>()
    private var isClicklAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.tracks = tracks
        binding.recyclerViewFavorites.adapter = adapter
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.getFavorites()
        viewModel.getFavoritesTracksLiveData().observe(viewLifecycleOwner) { list ->
            if (list.isEmpty()) showPlaceholder() else showFavorites(list)
        }
    }

    private fun showPlaceholder(){
        binding.recyclerViewFavorites.isVisible = false
        binding.nothingFoundFavorites.isVisible = true
    }

    // Показать список избранных треков
    @SuppressLint("NotifyDataSetChanged")
    private fun showFavorites(list: List<TrackDataClass>){
        binding.recyclerViewFavorites.isVisible = true
        binding.nothingFoundFavorites.isVisible = false
        tracks.clear()
        tracks.addAll(list)
        adapter.notifyDataSetChanged()
        binding.recyclerViewFavorites.scheduleLayoutAnimation()
    }

    // Переход к плееру
    private fun navigateToAudioPlayer(track: TrackDataClass) {
        val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
        intent.putExtra(KEY_FOR_PLAYER, TrackMapper.mapTrackDomainToUi(track))
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()

        if (isChangedFavorites) viewModel.getFavorites()
        isChangedFavorites = false
    }

    override fun onStop() {
        super.onStop()
        isClicklAllowed = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::binding.isInitialized) binding.recyclerViewFavorites.adapter = null
        tracks.clear()
    }
}