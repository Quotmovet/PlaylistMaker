package com.example.playlistmaker.activites.for_search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

class MusicListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val singerName: TextView = itemView.findViewById(R.id.singerName)
    private val trackTimeMillis: TextView = itemView.findViewById(R.id.trackTime)
    private val trackTitle: ImageView = itemView.findViewById(R.id.trackTitle)

    private val radius: Float = 2 * itemView.resources.displayMetrics.density

    fun bind(track: TrackDataClass) {

        // Трек
        if (track.trackName.isEmpty()) {
            trackName.setText(R.string.noReply)
        } else {
            trackName.text = track.trackName
        }

        // Исполнитель
        if (track.artistName.isEmpty()) {
            singerName.setText(R.string.noReply)
        } else {
            singerName.text = track.artistName
        }

        // Длительность
        trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        // Изображение
        val imageUrl = track.artworkUrl1100
        Glide.with(itemView)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_of_track)
            .transform(RoundedCorners(radius.toInt()))
            .fitCenter()
            .into(trackTitle)
    }
}