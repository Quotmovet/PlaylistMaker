package com.example.playlistmaker.search.ui.model

import com.example.playlistmaker.search.domain.model.TrackDataClass

data class TrackState(
    val tracks: List<TrackDataClass>,
    val isLoading: Boolean,
    val isFailed: Boolean?,
    val isEmpty: Boolean = false
)
