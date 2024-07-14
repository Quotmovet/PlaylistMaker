package com.example.playlistmaker.search.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackDataClass(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl1100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val trackId: Int
) : Parcelable