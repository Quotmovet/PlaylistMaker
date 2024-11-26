package com.example.playlistmaker.creatingPlaylist.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.R
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.creatingPlaylist.ui.state.StatePlaylistAdded
import com.example.playlistmaker.creatingPlaylist.ui.viewModel.EditPlaylistViewModel
import com.example.playlistmaker.root.ui.RootActivity
import com.example.playlistmaker.util.notification.Notification
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment: CreatingPlaylistFragment() {

    companion object {

        private const val EXTRA_PLAYLIST = "EXTRA_PLAYLIST"

        fun createArgs(playlist: PlaylistDataClass): Bundle =
            bundleOf(EXTRA_PLAYLIST to playlist)
    }

    override val viewModel by viewModel<EditPlaylistViewModel>()

    private var playlist: PlaylistDataClass? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? RootActivity)?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.GONE

        playlist = getPlaylist()

        binding.newPlaylistTitleText.text = getString(R.string.edit)
        binding.createButton.text = getString(R.string.save)

        binding.trackName.setText(playlist?.playlistName)
        binding.description.setText(playlist?.descriptionPlaylist)
        playlist?.uriOfImage?.toUri()?.let { showCover(it) }

        binding.createButton.setOnClickListener {
            savePlaylist()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isPlaylistUpdated.collect { state ->
                when (state) {
                    StatePlaylistAdded.SUCCESS -> {
                        Notification.make(binding.root, getString(R.string.playlist_updated), Snackbar.LENGTH_SHORT).show()
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                    StatePlaylistAdded.ERROR -> {
                        Notification.make(binding.root, getString(R.string.error_playlist_updated), Snackbar.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun savePlaylist() {
        playlist?.let {
            (viewModel).savePlaylist(it)
        }
    }

    private fun getPlaylist(): PlaylistDataClass? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(EXTRA_PLAYLIST, PlaylistDataClass::class.java)
        } else {
            requireArguments().getSerializable(EXTRA_PLAYLIST) as PlaylistDataClass
        }
    }
}