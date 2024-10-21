package com.example.playlistmaker.creatingPlaylist.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.creatingPlaylist.ui.PlaylistCreationHandler
import com.example.playlistmaker.creatingPlaylist.ui.state.StatePlaylistAdded
import com.example.playlistmaker.creatingPlaylist.ui.viewModel.CreatingPlaylistViewModel
import com.example.playlistmaker.databinding.FragmentCreatingPlaylistBinding
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.notification.Notification
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatingPlaylistFragment : Fragment() {

    companion object {
        var isCreatePlaylistFragmentFilled = false
        private const val URI = "uri"
        private const val ARG_TRACK = "arg_track"

        fun newInstance(track: TrackDataClass? = null): CreatingPlaylistFragment {
            return CreatingPlaylistFragment().apply {
                arguments = Bundle().apply {
                    track?.let { putParcelable(ARG_TRACK, it) }
                }
            }
        }
    }

    private var stringOfPlaylistUri: String = ""

    private val viewModel by viewModel<CreatingPlaylistViewModel>()

    private lateinit var binding: FragmentCreatingPlaylistBinding

    private var track: TrackDataClass? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatingPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // установка размера картинки
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                view.requestLayout()
            }
        })

        track = arguments?.getParcelable(ARG_TRACK)

        if (savedInstanceState != null) {
            val stringUri = savedInstanceState.getString(URI, "")
            if (stringUri.isNotEmpty()) {
                showCover(stringUri.toUri())
            }
        }

        setupObservers()
        setupListeners()
        setupTextWatchers()
        setupBackButton()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isPlaylistAdded.collect { state ->
                    handlePlaylistAddedState(state)
                }
            }
        }
    }

    // Обработка состояния создания плейлиста
    private fun handlePlaylistAddedState(state: StatePlaylistAdded) {
        val activity = requireActivity()
        when (state) {
            StatePlaylistAdded.SUCCESS -> {
                val playlistName = binding.trackName.text.toString()
                if (activity is PlaylistCreationHandler) {
                    activity.run { onPlaylistCreated(playlistName) }
                } else {
                    Notification.make(binding.root, "$playlistName ${getString(R.string.created)}", Snackbar.LENGTH_SHORT).show()
                }
                activity.supportFragmentManager.popBackStack()
                viewModel.resetState()
            }

            StatePlaylistAdded.ERROR -> {
                if (activity is PlaylistCreationHandler) {
                    activity.run { onPlaylistCreationError() }
                } else {
                    Notification.make(binding.root, getString(R.string.error_creating_playlist), Snackbar.LENGTH_SHORT).show()
                }
                viewModel.resetState()
            }

            StatePlaylistAdded.LOADING -> {
                // Реализовать потом
            }

            StatePlaylistAdded.NOTHING -> {
                // Ничего не делать
            }
        }
    }

    private fun setupListeners() {
        binding.mainBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                stringOfPlaylistUri = it.toString()
                showCover(it)
                viewModel.setUri(stringOfPlaylistUri)
            }
        }

        binding.trackTitle.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createButton.setOnClickListener {
            track?.let { viewModel.addNewPlaylist(it) } ?: viewModel.addNewPlaylist()
        }
    }

    private fun setupTextWatchers() {

        binding.trackName.addTextChangedListener(
            afterTextChanged = { s: Editable? ->
                if(!s.isNullOrEmpty()) {
                    binding.trackName.setBackgroundResource(R.drawable.text_filled_blue)
                    binding.inscriptionName.isVisible = true
                    binding.createButton.isEnabled = true
                    viewModel.setName(s.toString())
                } else {
                    binding.trackName.setBackgroundResource(R.drawable.text_filled)
                    binding.inscriptionName.isVisible = false
                    binding.createButton.isEnabled = false
                    viewModel.setName("")
                }
            }
        )

        binding.description.addTextChangedListener(
            afterTextChanged = { s: Editable? ->
                if(!s.isNullOrEmpty()) {
                    binding.description.setBackgroundResource(R.drawable.text_filled_blue)
                    binding.inscriptionDescription.isVisible = true
                    viewModel.setDescription(s.toString())
                } else {
                    binding.description.setBackgroundResource(R.drawable.text_filled)
                    binding.inscriptionDescription.isVisible = false
                    viewModel.setDescription("")
                }
            }
        )
    }

    private fun setupBackButton(){
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isCreatePlaylistFragmentFilled) {
                    showExitConfirmationDialog()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun showCover(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.rectangle_corner)
            .centerCrop()
            .into(binding.trackTitle)
    }

    private fun showExitConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.finish_creating_playlist))
            .setMessage(getString(R.string.data_will_be_lost))
            .setNeutralButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.complete)) { _, _ ->
                isCreatePlaylistFragmentFilled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(URI, stringOfPlaylistUri)
    }
}