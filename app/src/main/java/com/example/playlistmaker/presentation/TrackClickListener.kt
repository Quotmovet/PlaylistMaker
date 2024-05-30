package com.example.playlistmaker.presentation

import com.example.playlistmaker.domain.models.TrackDataClass

fun interface TrackClickListener {
    fun onTrackClick(track: TrackDataClass)
}