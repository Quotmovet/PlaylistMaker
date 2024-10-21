package com.example.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.ui.adapter.PlaylistAdapter
import com.example.playlistmaker.media.ui.viewmodel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistFragment()
    }
    private lateinit var binding: FragmentPlaylistBinding

    private val viewModel by viewModel<PlaylistViewModel>()

    private lateinit var  adapter: PlaylistAdapter

    private val playlists = arrayListOf<PlaylistDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment_to_newPlaylistFragment)
        }

        adapter = PlaylistAdapter ()
        adapter.playlists = playlists

        binding.recyclerViewOfPlaylists.adapter = adapter
        binding.recyclerViewOfPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.getPlaylists()

        // Переключение режимов экрана
        viewModel.getPlaylistsLiveData().observe(viewLifecycleOwner) { list ->
            if (list.isEmpty()) showPlaceholder() else showPlaylists(list)
        }
    }

    private fun showPlaceholder () {
        binding.recyclerViewOfPlaylists.isVisible = false
        binding.nothingFound.isVisible = true
    }

    private fun showPlaylists (list: List<PlaylistDataClass>) {
        binding.recyclerViewOfPlaylists.isVisible = true
        binding.nothingFound.isVisible = false
        playlists.clear()
        playlists.addAll(list)
        adapter.notifyDataSetChanged()
    }
}