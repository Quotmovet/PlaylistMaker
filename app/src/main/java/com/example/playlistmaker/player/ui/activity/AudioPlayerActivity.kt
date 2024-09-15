package com.example.playlistmaker.player.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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

var isChangedFavorites: Boolean = false

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioplayerBinding
    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(intent.getParcelableExtraCompat<TrackDataClass>(KEY_FOR_PLAYER))
    }

    private var favoritesButtonState = false

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
        binding.likeButton.setOnClickListener{
            viewModel.onFavoriteClicked()
            isChangedFavorites = true
        }
    }

    // Реализация наблюдателя за ViewModel
    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        viewModel.trackInfo.observe(this) { track -> bind(track) }
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

    // Метод изменения кнопки "Избранное"
    private fun changeLikeButton(isFavorite: Boolean) {
        if (isFavorite) {
            binding.likeButton.setImageResource(R.drawable.heart_fill)
        } else {
            binding.likeButton.setImageResource(R.drawable.heart_border)
        }
    }

    // Завершение активности
    override fun finish() {
        val resultIntent = Intent().apply {
            putExtra("trackId", viewModel.trackInfo.value?.trackId)
            putExtra("isFavorite", viewModel.getTrackIsFavoriteLiveData().value)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        super.finish()
    }
}