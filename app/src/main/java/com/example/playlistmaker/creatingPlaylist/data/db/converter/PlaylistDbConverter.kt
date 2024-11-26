package com.example.playlistmaker.creatingPlaylist.data.db.converter

import com.example.playlistmaker.creatingPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.creatingPlaylist.domain.model.PlaylistDataClass
import com.google.gson.Gson

class PlaylistDbConverter(private val gson: Gson) {

    fun map(playlist: PlaylistDataClass): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.playlistName,
            playlist.descriptionPlaylist,
            playlist.uriOfImage,
            gson.toJson(playlist.tracksListId),
            playlist.trackCount
        )
    }

    fun map(playlist: PlaylistEntity): PlaylistDataClass {
        return PlaylistDataClass(
            playlist.id,
            playlist.playlistName,
            playlist.descriptionPlaylist,
            playlist.uriOfImage,
            gson.fromJson(playlist.tracksListId, Array<Int>::class.java).toCollection(ArrayList()),
            playlist.trackCount
        )
    }
}