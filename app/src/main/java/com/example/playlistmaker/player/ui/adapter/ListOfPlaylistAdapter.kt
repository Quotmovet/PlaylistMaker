package com.example.playlistmaker.player.ui.adapter

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
import com.example.playlistmaker.util.other.StringsUtil

class ListOfPlaylistAdapter(private val clickListener: SmallPlaylistClickListener)
    : RecyclerView.Adapter<ListOfPlaylistAdapter.ListOfPlaylistViewHolder>() {

    var playlists = ArrayList<PlaylistDataClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfPlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_element_little_playlist, parent, false)
        return ListOfPlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: ListOfPlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener { clickListener.onSmallPlaylistClick(playlist) }
    }

    inner class ListOfPlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {
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
            playlistId = playlist.id
            playlistName.text = playlist.playlistName

            trackCount.text = StringsUtil.countTracks(playlist.trackCount)
        }
    }

    fun interface SmallPlaylistClickListener {
        fun onSmallPlaylistClick(playlist: PlaylistDataClass)
    }
}