package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MusicListAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<MusicListViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tracks_view_element, parent, false)
        return MusicListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicListViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}