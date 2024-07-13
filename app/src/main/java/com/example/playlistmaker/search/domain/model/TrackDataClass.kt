package com.example.playlistmaker.search.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackDataClass(
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackTimeMillis: String,
    @SerializedName("artworkUrl100") val artworkUrl1100: String,
    @SerializedName("collectionName") val collectionName: String,
    @SerializedName("releaseDate") val releaseDate: String,
    @SerializedName("primaryGenreName") val primaryGenreName: String,
    @SerializedName("country") val country: String,
    @SerializedName("previewUrl") val previewUrl: String,
    @SerializedName("trackId") val trackId: Int
) : Parcelable