package com.example.playlistmaker.player.domain.models

data class TrackModel(
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
)
