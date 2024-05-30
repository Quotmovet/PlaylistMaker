package com.example.playlistmaker.ui.movies

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.TrackDataClass
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {

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

    private lateinit var timeCode: TextView
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private var playerState = STATE_DEFAULT
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audioplayer_activity)

        playButton = findViewById(R.id.playButton)
        likeButton = findViewById(R.id.likeButton)

        trackTitle = findViewById(R.id.trackTitle)
        trackName = findViewById(R.id.trackName)
        singerName = findViewById(R.id.singerName)
        trackTimeMillis = findViewById(R.id.trackTime)
        albumTrackName = findViewById(R.id.albumTrackName)
        yearTrackDate = findViewById(R.id.yearTrackDate)
        genreTrackName = findViewById(R.id.genreTrackName)
        countryTrackName = findViewById(R.id.countryTrackName)
        timeCode = findViewById(R.id.timeCode)

        val arrowBack: ImageView = findViewById(R.id.main_back_button)

        val track = intent.getSerializableExtra(KEY_FOR_INTENT) as? TrackDataClass
        if (track != null) {
            bind(track)
        } else {
            Toast.makeText(this, getString(R.string.uploadFailed), Toast.LENGTH_SHORT).show()
        }

        // вернуться назад
        arrowBack.setOnClickListener {
            finish()
        }

        if (track != null) {
            preparePlayer(track.previewUrl)
        }

        playButton.setOnClickListener {
            playbackControl()
        }
    }

    private fun bind(track: TrackDataClass) {

        // Трек
        trackName.text = track.trackName
            .takeIf { it.isNotEmpty() } ?: getString(R.string.noReply)

        // Исполнитель
        singerName.text = track.artistName
            .takeIf { it.isNotEmpty() } ?: getString(R.string.noReply)

        // Длительность
        trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(track.trackTimeMillis)

        // Изображение
        val density = resources.displayMetrics.density
        val radiusInPixels = (8 * density).toInt()
        val imageUrl = track.artworkUrl1100
        val newDPI = imageUrl.replaceAfterLast('/', "512x512bb.jpg")
        Glide.with(this)
            .load(newDPI)
            .placeholder(R.drawable.placeholder_of_track)
            .fitCenter()
            .transform(RoundedCorners(radiusInPixels))
            .into(trackTitle)

        // Название альбома
        val albumTrack: TextView = findViewById(R.id.albumTrack)
        if (track.collectionName.isEmpty()) {
            albumTrackName.visibility = View.GONE
            albumTrack.visibility = View.GONE
        } else {
            albumTrackName.text = track.collectionName
            albumTrackName.visibility = View.VISIBLE
            albumTrack.visibility = View.VISIBLE
        }

        // Год
        val releaseDate = track.releaseDate
        val year = releaseDate.substring(0, 4)
        yearTrackDate.text = year.takeIf { it.isNotEmpty() } ?: getString(R.string.noReply)

        // Жанр
        genreTrackName.text = track.primaryGenreName
            .takeIf { it.isNotEmpty() } ?: getString(R.string.noReply)

        // Страна
        countryTrackName.text = track.country
            .takeIf { it.isNotEmpty() } ?: getString(R.string.noReply)

        // Длительность
        timeCode.text = dateFormat.format(track.trackTimeMillis)
    }

    // медиаплеер
    private fun preparePlayer(track: String) {
        mediaPlayer.setDataSource(track)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
            if (currentPosition > 0) {
                mediaPlayer.seekTo(currentPosition)
                updatePlaybackTime()
                currentPosition = 0
            }
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            playButton.setImageResource(R.drawable.play)
            resetPlayer()
        }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            } STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handlerCallBack()
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.button_paused)
        playerState = STATE_PLAYING
        handler.post(runTime())
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
        handlerCallBack()
    }

    private fun resetPlayer() {
        playButton.setImageResource(R.drawable.play)
        playButton.isEnabled = true
        mediaPlayer.seekTo(0)
        playerState = STATE_PAUSED
        timeCode.text = "00:00"
        handlerCallBack()
    }

    private fun runTime(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    updatePlaybackTime()

                    if (mediaPlayer.currentPosition >= mediaPlayer.duration) {
                        resetPlayer()
                    } else {
                        handler.postDelayed(this, SET_TIME_WAIT)
                    }
                }
            }
        }
    }

    private fun handlerCallBack() {
        handler.removeCallbacks(runTime())
    }

    // сохранение состояний
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_PLAYER_STATE, playerState)
        outState.putInt(KEY_CURRENT_POSITION, mediaPlayer.currentPosition)
        outState.putString(KEY_TIME_CODE, timeCode.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        playerState = savedInstanceState.getInt(KEY_PLAYER_STATE, STATE_DEFAULT)
        currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0)
        timeCode.text = savedInstanceState.getString(KEY_TIME_CODE, "00:00")
        when (playerState) {
            STATE_PLAYING -> {
                mediaPlayer.seekTo(currentPosition)
                startPlayer()
            } STATE_PAUSED -> {
                mediaPlayer.seekTo(currentPosition)
                playButton.setImageResource(R.drawable.play)
                updatePlaybackTime()
            } STATE_PREPARED -> {
                playButton.setImageResource(R.drawable.play)
                updatePlaybackTime()
            }
        }
    }

    private fun updatePlaybackTime() {
        timeCode.text = dateFormat.format(mediaPlayer.currentPosition)
    }

    companion object {
        private const val KEY_FOR_INTENT = "key_for_intent"
        private const val KEY_PLAYER_STATE = "player_state"
        private const val KEY_CURRENT_POSITION = "current_position"
        private const val KEY_TIME_CODE = "time_code"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val SET_TIME_WAIT = 350L
    }
}