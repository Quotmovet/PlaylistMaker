package com.example.playlistmaker.search.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class TrackDataClassDto(
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