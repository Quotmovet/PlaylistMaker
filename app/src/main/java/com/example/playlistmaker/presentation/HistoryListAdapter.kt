package com.example.playlistmaker.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.TrackDataClass

class HistoryListAdapter(private val tracksHistory: List<TrackDataClass>?,
                         private val trackClickListener: TrackClickListener):
    RecyclerView.Adapter<MusicListViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tracks_view_element, parent, false)
        return MusicListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicListViewHolder, position: Int) {
        val track = tracksHistory?.get(position)
        if (track != null) {
            holder.bind(track)
        }

        holder.itemView.setOnClickListener {
            if (track != null) {
                trackClickListener.onTrackClick(track)
            }
        }
    }

    override fun getItemCount(): Int {
        return tracksHistory?.count() ?: 0
    }
}