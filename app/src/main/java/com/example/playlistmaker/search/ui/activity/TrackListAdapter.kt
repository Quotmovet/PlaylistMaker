package com.example.playlistmaker.search.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.model.TrackDataClass
import java.text.SimpleDateFormat
import java.util.Locale

class TrackListAdapter(private val trackClickListener: TrackClickListener
): RecyclerView.Adapter<TrackListAdapter.MusicListViewHolder>() {

    var tracks = ArrayList<TrackDataClass>()

    fun interface TrackClickListener {
        fun onTrackClick(track: TrackDataClass)
    }

    class MusicListViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.tracks_view_element, parent, false)) {

        private val trackName: TextView = itemView.findViewById(R.id.trackName)
        private val singerName: TextView = itemView.findViewById(R.id.singerName)
        private val trackTimeMillis: TextView = itemView.findViewById(R.id.trackTime)
        private val trackTitle: ImageView = itemView.findViewById(R.id.trackTitle)

        private val radius: Float = 2 * itemView.resources.displayMetrics.density

        fun bind(track: TrackDataClass) {

            val imageUrl = track.artworkUrl1100

            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_of_track)
                .fitCenter()
                .transform(RoundedCorners(radius.toInt()))
                .into(trackTitle)

            singerName.text = track.artistName
            trackName.text = track.trackName
            trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(track.trackTimeMillis.toLong())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MusicListViewHolder(parent)

    override fun onBindViewHolder(holder: MusicListViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            trackClickListener.onTrackClick(tracks[position]) }
    }

    override fun getItemCount() = tracks.size
}