package com.example.playlistmaker.activites.for_search

import com.google.gson.annotations.SerializedName

data class TrackDataClass(
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackTimeMillis: Int,
    @SerializedName("artworkUrl100") val artworkUrl1100: String,
    @SerializedName("trackId") val trackId: Int
    )