package com.example.playlistmaker.activites.for_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

class HistoryListAdapter(private val tracksHistory: List<TrackDataClass>?,
                         private val trackClickListener: TrackClickListener) :
    RecyclerView.Adapter<MusicListViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tracks_view_element, parent, false)
        return MusicListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicListViewHolder, position: Int) {
        val track = tracksHistory!![position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            trackClickListener.onTrackClick(track)
        }
    }

    override fun getItemCount(): Int {
        return tracksHistory!!.count()
    }
}