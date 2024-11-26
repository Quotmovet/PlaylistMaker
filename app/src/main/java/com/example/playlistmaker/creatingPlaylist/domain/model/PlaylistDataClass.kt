package com.example.playlistmaker.creatingPlaylist.domain.model

import java.io.Serializable

data class PlaylistDataClass(
    val id: Int,   // id плейлиста
    val playlistName: String,
    val descriptionPlaylist: String?,
    val uriOfImage: String?,
    val tracksListId: MutableList<Int>,
    var trackCount: Int
): Serializable
