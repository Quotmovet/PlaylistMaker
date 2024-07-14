package com.example.playlistmaker.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TracksViewElementBinding
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.mapper.TrackMapper
import java.text.SimpleDateFormat
import java.util.Locale

class TrackListAdapter(private val trackClickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackListAdapter.MusicListViewHolder>() {

    var tracks = ArrayList<TrackDataClass>()

    fun interface TrackClickListener {
        fun onTrackClick(track: TrackDataClass)
    }

    class MusicListViewHolder(private val binding: TracksViewElementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val radius: Float = 2 * itemView.resources.displayMetrics.density

        fun bind(track: TrackDataClass) {
            val uiTrack = TrackMapper.mapTrackDomainToUi(track)

            with(binding) {
                singerName.text = uiTrack.artistName
                trackName.text = uiTrack.trackName
                trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(uiTrack.trackTimeMillis.toLong())

                Glide.with(itemView)
                    .load(uiTrack.artworkUrl1100)
                    .placeholder(R.drawable.placeholder_of_track)
                    .fitCenter()
                    .transform(RoundedCorners(radius.toInt()))
                    .into(trackTitle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TracksViewElementBinding.inflate(inflater, parent, false)
        return MusicListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicListViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            trackClickListener.onTrackClick(track)
        }
    }

    override fun getItemCount() = tracks.size
}