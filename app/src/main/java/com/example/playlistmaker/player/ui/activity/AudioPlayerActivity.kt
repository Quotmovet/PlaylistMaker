package com.example.playlistmaker.player.ui.activity

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.models.TrackModel
import com.example.playlistmaker.player.ui.viewModel.AudioPlayerViewModel
import com.example.playlistmaker.player.ui.viewModel.AudioPlayerViewModelFactory
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.search.ui.viewModel.Constatn.KEY_FOR_PLAYER

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var playButton: ImageButton
    private lateinit var likeButton: ImageButton
    private lateinit var addButton: ImageButton

    private lateinit var trackTitle: ImageView
    private lateinit var trackName: TextView
    private lateinit var singerName: TextView
    private lateinit var trackTimeMillis: TextView
    private lateinit var albumTrackName: TextView
    private lateinit var yearTrackDate: TextView
    private lateinit var genreTrackName: TextView
    private lateinit var countryTrackName: TextView

    private lateinit var arrowBack: ImageView

    private lateinit var timeCode: TextView

    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audioplayer_activity)

        initViews()
        setupListeners()

        val track = if (SDK_INT >= TIRAMISU) {
            intent.getParcelableExtra(KEY_FOR_PLAYER, TrackDataClass::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(KEY_FOR_PLAYER) as? TrackDataClass
        }

        if (track != null) {
            viewModel = ViewModelProvider(this,
                AudioPlayerViewModelFactory(convertTrack(track)))[AudioPlayerViewModel::class.java]
            observeViewModel()
        } else {
            Toast.makeText(this,
                getString(R.string.uploadFailed),
                Toast.LENGTH_SHORT).show()
            PlayerState.STOPPED
            PlayerState.ERROR
            finish()
        }
    }

    // Инициализация UI компонентов
    private fun initViews() {
        playButton = findViewById(R.id.playButton)
        likeButton = findViewById(R.id.likeButton)
        addButton = findViewById(R.id.addButton)
        trackTitle = findViewById(R.id.trackTitle)
        trackName = findViewById(R.id.trackName)
        singerName = findViewById(R.id.singerName)
        trackTimeMillis = findViewById(R.id.trackTime)
        albumTrackName = findViewById(R.id.albumTrackName)
        yearTrackDate = findViewById(R.id.yearTrackDate)
        genreTrackName = findViewById(R.id.genreTrackName)
        countryTrackName = findViewById(R.id.countryTrackName)
        timeCode = findViewById(R.id.timeCode)
        arrowBack = findViewById(R.id.main_back_button)
    }

    // Настройка слушателей
    private fun setupListeners() {
        arrowBack.setOnClickListener { finish() }
        playButton.setOnClickListener { viewModel.playbackControl() }
    }

    // Реализация наблюдателя за ViewModel
    private fun observeViewModel() {
        viewModel.trackInfo.observe(this) { track -> bind(track) }

        viewModel.playerState.observe(this) { state ->
            when (state) {
                PlayerState.STARTED -> playButton.setImageResource(R.drawable.button_paused)
                PlayerState.PAUSED, PlayerState.PREPARED, PlayerState.STOPPED, PlayerState.COMPLETED ->
                    playButton.setImageResource(R.drawable.play)
                else -> { /* Не делаем ничего */ }
            }
        }

        viewModel.isTrackCompleted.observe(this) { isCompleted ->
            if (isCompleted) {
                timeCode.text = "00:00"
                playButton.setImageResource(R.drawable.play)
            }
        }

        viewModel.currentPosition.observe(this) { formattedTime ->
            timeCode.text = formattedTime
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    // viewModel.release() вызывается автоматически в onCleared(), метод оставил на всякий случай
    override fun onDestroy() {
        super.onDestroy()
    }

    // Получение Parcelable из Intent
    private fun <T : Parcelable> Intent.getParcelable(key: String, clazz: Class<T>): T? {
        return if (SDK_INT >= TIRAMISU) {
            this.getParcelableExtra(key, clazz)
        } else {
            @Suppress("DEPRECATION")
            this.getParcelableExtra(key) as? T
        }
    }

    // Конвертирование TrackDataClass в TrackModel для использования в ViewModel
    private fun convertTrack(track: TrackDataClass): TrackModel {
        return TrackModel(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl1100 = track.artworkUrl1100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            trackId = track.trackId,
        )
    }

    // Инициализация UI компонентов и привязка к данным из ViewModel
    private fun bind(track: TrackModel) {
        trackName.text = track.trackName
        singerName.text = track.artistName
        trackTimeMillis.text = track.trackTimeMillis

        val albumTrack: TextView = findViewById(R.id.albumTrack)
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

        val density = resources.displayMetrics.density
        val radiusInPixels = (8 * density).toInt()
        Glide.with(this)
            .load(track.artworkUrl1100)
            .placeholder(R.drawable.placeholder_of_track)
            .transform(RoundedCorners(radiusInPixels))
            .into(trackTitle)
    }
}