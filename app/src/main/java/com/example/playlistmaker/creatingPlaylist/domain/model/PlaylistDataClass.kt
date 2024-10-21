package com.example.playlistmaker.creatingPlaylist.domain.model

data class PlaylistDataClass(
    val id: Int,   // id плейлиста
    val playlistName: String,
    val descriptionPlaylist: String?,
    val uriOfImage: String?,
    val tracksListId: MutableList<Int>,
    var trackCount: Int
)
