package com.example.playlistmaker.player.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.player.ui.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.Constatn.KEY_FOR_PLAYER
import com.example.playlistmaker.util.extensions.dpToPx
import com.example.playlistmaker.util.extensions.getParcelableExtraCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioplayerBinding
    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(intent.getParcelableExtraCompat<TrackDataClass>(KEY_FOR_PLAYER))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    // Настройка слушателей
    private fun setupListeners() {
        binding.mainBackButton.setOnClickListener { finish() }
        binding.playButton.setOnClickListener { viewModel.onPlayButtonClicked() }
    }

    // Реализация наблюдателя за ViewModel
    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        viewModel.trackInfo.observe(this) { track -> bind(track) }

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
}