package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class MusicListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val singerName: TextView = itemView.findViewById(R.id.singerName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val trackTitle: ImageView = itemView.findViewById(R.id.trackTitle)

    fun bind(track: Track) {

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
        if (track.trackTime.isEmpty()) {
            trackTime.setText(R.string.noReply)
        } else {
            trackTime.text = track.trackTime
        }

        // Изображение
        val imageUrl = track.artworkUrl100
        Glide.with(itemView)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_of_track)
            .transform(RoundedCorners(2))
            .fitCenter()
            .into(trackTitle)
    }
}