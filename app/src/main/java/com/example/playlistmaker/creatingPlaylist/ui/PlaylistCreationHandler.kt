package com.example.playlistmaker.creatingPlaylist.ui

interface PlaylistCreationHandler {
    fun onPlaylistCreated(playlistName: String)
    fun onPlaylistCreationError()
}