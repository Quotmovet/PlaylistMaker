package com.example.playlistmaker.activites.for_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import api_itunes.SearchTrackResponse
import com.example.playlistmaker.R

class MusicListAdapter(private val tracks: SearchTrackResponse?) :
    RecyclerView.Adapter<MusicListViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tracks_view_element, parent, false)
        return MusicListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicListViewHolder, position: Int) {
        holder.bind(tracks!!.results[position])
    }

    override fun getItemCount(): Int {
        return tracks!!.resultCount
    }
}