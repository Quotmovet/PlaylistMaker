package com.example.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks_table")
data class TrackEntity(
    @PrimaryKey (autoGenerate = true)
    val id: Int,         // Первичный ключ
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl1100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val trackId: Int,
)
