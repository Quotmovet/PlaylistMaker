package com.example.playlistmaker.activites.for_search

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity(){

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audioplayer_activity)

        trackTitle = findViewById(R.id.trackTitle)
        trackName = findViewById(R.id.trackName)
        singerName = findViewById(R.id.singerName)
        trackTimeMillis = findViewById(R.id.trackTime)
        albumTrackName = findViewById(R.id.albumTrackName)
        yearTrackDate= findViewById(R.id.yearTrackDate)
        genreTrackName = findViewById(R.id.genreTrackName)
        countryTrackName = findViewById(R.id.countryTrackName)
        timeCode = findViewById(R.id.timeCode)

        val arrowBack : ImageView= findViewById(R.id.main_back_button)

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
    }

    private fun bind(track: TrackDataClass){

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
        val newDPI = imageUrl.replaceAfterLast('/',"512x512bb.jpg")
        Glide.with(this)
            .load(newDPI)
            .placeholder(R.drawable.placeholder_of_track)
            .transform(RoundedCorners(radiusInPixels))
            .fitCenter()
            .into(trackTitle)

        // Название альбома
        val albumTrack : TextView = findViewById(R.id.albumTrack)
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

    companion object{
        private const val KEY_FOR_INTENT = "key_for_intent"
    }
}