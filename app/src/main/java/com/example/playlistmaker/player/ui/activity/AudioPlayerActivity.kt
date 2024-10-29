package com.example.playlistmaker.player.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.example.playlistmaker.creatingPlaylist.ui.fragment.CreatingPlaylistFragment
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.media.ui.viewmodel.PlaylistViewModel
import com.example.playlistmaker.player.ui.adapter.ListOfPlaylistAdapter
import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.player.ui.viewModel.AddTrackStatus
import com.example.playlistmaker.player.ui.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.Constatn.KEY_FOR_PLAYER
import com.example.playlistmaker.util.extensions.dpToPx
import com.example.playlistmaker.util.extensions.getParcelableExtraCompat
import com.example.playlistmaker.util.notification.Notification
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

var isChangedFavorites: Boolean = false

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioplayerBinding
    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(intent.getParcelableExtraCompat<TrackDataClass>(KEY_FOR_PLAYER))
    }

    private var favoritesButtonState = false
    private var track: TrackDataClass? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var adapter: ListOfPlaylistAdapter
    private val playlistViewModel by viewModel<PlaylistViewModel>()

    private val playlists = mutableListOf<PlaylistDataClass>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
        bottomSheetBehaviorStart()
        setupAdapter()
    }

    // Настройка слушателей
    private fun setupListeners() {
        binding.mainBackButton.setOnClickListener { finish() }
        binding.playButton.setOnClickListener { viewModel.onPlayButtonClicked() }
        binding.addButton.setOnClickListener{ bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED }
        binding.creatPlaylistButton.setOnClickListener {
            track?.let { currentTrack -> openCreatingPlaylistFragment(currentTrack) } ?: run {
                showMessage(getString(R.string.no_track_selected))
            }
        }
        binding.likeButton.setOnClickListener{
            viewModel.onFavoriteClicked()
            isChangedFavorites = true
        }
    }

    // Реализация наблюдателя за ViewModel
    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        viewModel.trackInfo.observe(this) { newTrack ->
            bind(newTrack)
            track = newTrack
        }
        viewModel.isFavorite.observe(this) { isFavorite ->
            favoritesButtonState = isFavorite
            changeLikeButton(isFavorite) }

        viewModel.getTrackStateAudioPlayerLiveData().observe(this) { state ->
            when (state) {
                is PlayerState.Playing -> {
                    binding.playButton.setImageResource(R.drawable.button_paused)
                    binding.timeCode.text = state.progress
                }
                is PlayerState.Paused -> {
                    binding.playButton.setImageResource(R.drawable.play)
                    binding.timeCode.text = state.progress
                }
                is PlayerState.Prepared, is PlayerState.Default -> {
                    binding.playButton.setImageResource(R.drawable.play)
                    binding.timeCode.text = "00:00"
                }
                else -> { /* Не делаем ничего */ }
            }
        }
        viewModel.getTrackIsFavoriteLiveData().observe(this) { isFavorite ->
            favoritesButtonState = isFavorite
            changeLikeButton(favoritesButtonState)
        }
    }

    // Настройка BottomSheet
    private fun bottomSheetBehaviorStart(){

        val bottomSheetContainer = findViewById<LinearLayout>(R.id.standard_bottom_sheet)
        val overlay = findViewById<View>(R.id.overlay)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> overlay.visibility = View.GONE
                    else -> overlay.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset
            }
        })
    }

    private fun setupAdapter() {

        adapter = ListOfPlaylistAdapter { playlist ->
            track?.let { currentTrack ->
                viewModel.addTrackToPlaylist(playlist, currentTrack)
            }
        }

        adapter.playlists = playlists as ArrayList<PlaylistDataClass>

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        playlistViewModel.getPlaylists()

        // Переключение режимов экрана
        playlistViewModel.getPlaylistsLiveData().observe(this) { list ->
            playlists.clear()
            playlists.addAll(list)
            adapter.notifyDataSetChanged()
        }

        // Наблюдение за статусом добавления трека
        viewModel.addTrackStatus.observe(this) { status ->
            when (status) {
                is AddTrackStatus.Success -> {
                    showMessage(getString(R.string.track_added_to_playlist) + " " + status.playlistName)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
                is AddTrackStatus.AlreadyExists -> {
                    showMessage(getString(R.string.already_added) + " " + status.playlistName)
                }
                is AddTrackStatus.Error -> {
                    showMessage(getString(R.string.error_adding_track))
                }
                else -> {}
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    // Инициализация UI компонентов и привязка к данным из ViewModel
    private fun bind(track: TrackDataClass) {
        val highResImageUrl = track.artworkUrl1100.replace("100x100bb", "512x512bb")

        with(binding) {
            trackName.text = track.trackName
            singerName.text = track.artistName
            trackTime.text = track.trackTimeMillis

            if (track.collectionName.isEmpty()) {
                albumTrackName.isVisible = false
                albumTrack.isVisible = false
            } else {
                albumTrackName.text = track.collectionName
                albumTrackName.isVisible = true
                albumTrack.isVisible = true
            }

            yearTrackDate.text = track.releaseDate
            genreTrackName.text = track.primaryGenreName
            countryTrackName.text = track.country

            val radiusInPixels = dpToPx(8)
            Glide.with(this@AudioPlayerActivity)
                .load(highResImageUrl)
                .placeholder(R.drawable.placeholder_of_track)
                .transform(RoundedCorners(radiusInPixels))
                .into(trackTitle)
        }
    }

    private fun changeLikeButton(isFavorite: Boolean) {
        binding.likeButton.setImageResource(
            when (isFavorite) {
                true -> R.drawable.heart_fill
                false -> R.drawable.heart_border
            }
        )
    }

    private fun openCreatingPlaylistFragment(track: TrackDataClass) {
        val fragment = CreatingPlaylistFragment.newInstance(track)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
        }
    }

    private fun showMessage(message: String) {
        Notification.make(binding.root, message, Toast.LENGTH_SHORT).show()
    }

    // Завершение активности
    override fun finish() {
        val resultIntent = Intent().apply {
            putExtra(TRACK_ID, viewModel.trackInfo.value?.trackId)
            putExtra(IS_FAVORITE, viewModel.getTrackIsFavoriteLiveData().value)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        super.finish()
    }

    companion object {
        private const val TRACK_ID = "trackId"
        private const val IS_FAVORITE = "isFavorite"
    }
}