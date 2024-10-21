package com.example.playlistmaker.media.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass

class PlaylistAdapter : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    var playlists = ArrayList<PlaylistDataClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_element_big_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    inner class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var playlistId: Int? = null
        private val playlistTitle: ImageView = itemView.findViewById(R.id.playlistTitle)
        private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
        private val trackCount: TextView = itemView.findViewById(R.id.numbersOfTracks)

        @SuppressLint("SetTextI18n")
        fun bind(playlist: PlaylistDataClass) {
            playlistId = playlist.id
            Glide.with(itemView)
                .load(playlist.uriOfImage)
                .placeholder(R.drawable.placeholder_of_track)
                .centerCrop()
                .into(playlistTitle)
            playlistName.text = playlist.playlistName

            val list = playlist.trackCount
            val string = when(list % 10) {
                1 -> " трек"
                2, 3, 4 -> " трека"
                else -> " треков"
            }
            trackCount.text = list.toString() + string
        }
    }
}