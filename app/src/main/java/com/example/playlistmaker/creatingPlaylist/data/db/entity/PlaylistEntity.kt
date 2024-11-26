package com.example.playlistmaker.creatingPlaylist.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,         // Первичный ключ
    val playlistName: String,
    val descriptionPlaylist: String?,
    val uriOfImage: String?,
    val tracksListId: String,
    var trackCount: Int
)