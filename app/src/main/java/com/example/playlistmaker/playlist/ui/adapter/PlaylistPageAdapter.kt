package com.example.playlistmaker.playlist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ViewElementTrackBinding
import com.example.playlistmaker.search.domain.model.TrackDataClass
import com.example.playlistmaker.util.mapper.TrackMapper

class PlaylistPageAdapter(
    private val clickListener: TrackClickListener,
    private val longClickListener: TrackLongClickListener
) : RecyclerView.Adapter<PlaylistPageAdapter.MusicListViewHolder>() {

    var tracks = mutableListOf<TrackDataClass>()

    class MusicListViewHolder(private val binding: ViewElementTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val radius: Float = 2 * itemView.resources.displayMetrics.density

        fun bind(track: TrackDataClass) {
            val uiTrack = TrackMapper.mapTrackDomainToUi(track)

            with(binding) {
                singerName.text = uiTrack.artistName
                trackName.text = uiTrack.trackName
                trackTime.text = uiTrack.trackTimeMillis

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
        val binding = ViewElementTrackBinding.inflate(inflater, parent, false)
        return MusicListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicListViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { clickListener.onTrackClick(track) }
        holder.itemView.setOnLongClickListener { longClickListener.onTrackLongClick(track) }
    }

    override fun getItemCount() = tracks.size

    // Интерфейсы для обработки кликов
    fun interface TrackClickListener {
        fun onTrackClick(track: TrackDataClass)
    }

    fun interface TrackLongClickListener {
        fun onTrackLongClick(track: TrackDataClass): Boolean
    }
}