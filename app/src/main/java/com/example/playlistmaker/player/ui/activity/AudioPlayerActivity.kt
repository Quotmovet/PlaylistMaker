package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.AudioplayerActivityBinding
import com.example.playlistmaker.util.mapper.TrackMapper
import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.player.ui.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.player.ui.viewModel.AudioPlayerViewModelFactory
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.Constatn.KEY_FOR_PLAYER
import com.example.playlistmaker.util.extensions.dpToPx
import com.example.playlistmaker.util.extensions.getParcelableExtraCompat


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: AudioplayerActivityBinding
    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioplayerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

        val track = intent.getParcelableExtraCompat(KEY_FOR_PLAYER, TrackDataClass::class.java)

        if (track != null) {
            val trackModel = TrackMapper.mapTrackDomainToUi(track)
            viewModel = ViewModelProvider(this,
                AudioPlayerViewModelFactory(trackModel))[AudioPlayerViewModel::class.java]
            observeViewModel()
        } else {
            Toast.makeText(this,
                getString(R.string.uploadFailed),
                Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // Настройка слушателей
    private fun setupListeners() {
        binding.mainBackButton.setOnClickListener { finish() }
        binding.playButton.setOnClickListener { viewModel.playbackControl() }
    }

    // Реализация наблюдателя за ViewModel
    private fun observeViewModel() {
        viewModel.trackInfo.observe(this) { track -> bind(track) }

        viewModel.playerState.observe(this) { state ->
            when (state) {
                PlayerState.STARTED -> binding.playButton.setImageResource(R.drawable.button_paused)
                PlayerState.PAUSED, PlayerState.PREPARED, PlayerState.COMPLETED ->
                    binding.playButton.setImageResource(R.drawable.play)
                else -> { /* Не делаем ничего */ }
            }
        }

        viewModel.isCompleteAndCurrentPositionState.observe(this) { (isCompleted, position) ->
            if (isCompleted) {
                binding.timeCode.text = "00:00"
                binding.playButton.setImageResource(R.drawable.play)
            } else {
                binding.timeCode.text = position
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    // Инициализация UI компонентов и привязка к данным из ViewModel
    private fun bind(track: TrackDataClass) {
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
                .load(track.artworkUrl1100)
                .placeholder(R.drawable.placeholder_of_track)
                .transform(RoundedCorners(radiusInPixels))
                .into(trackTitle)
        }
    }
}