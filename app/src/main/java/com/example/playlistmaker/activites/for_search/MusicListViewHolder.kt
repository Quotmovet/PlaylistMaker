package com.example.playlistmaker.activites.for_search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

class MusicListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val singerName: TextView = itemView.findViewById(R.id.singerName)
    private val trackTimeMillis: TextView = itemView.findViewById(R.id.trackTime)
    private val trackTitle: ImageView = itemView.findViewById(R.id.trackTitle)

    fun bind(trackDataClass: TrackDataClass) {

        // Трек
        if (trackDataClass.trackName.isEmpty()) {
            trackName.setText(R.string.noReply)
        } else {
            trackName.text = trackDataClass.trackName
        }

        // Исполнитель
        if (trackDataClass.artistName.isEmpty()) {
            singerName.setText(R.string.noReply)
        } else {
            singerName.text = trackDataClass.artistName
        }

        // Длительность
        if (trackDataClass.trackTimeMillis.isEmpty()) {
            trackTimeMillis.setText(R.string.noReply)
        } else {
            trackTimeMillis.text = trackDataClass.trackTimeMillis
        }

        // Изображение
        val imageUrl = trackDataClass.artworkUrl100
        Glide.with(itemView)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_of_track)
            .transform(RoundedCorners(2))
            .fitCenter()
            .into(trackTitle)
    }
}