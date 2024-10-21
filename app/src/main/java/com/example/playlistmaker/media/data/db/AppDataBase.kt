package com.example.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.creatingPlaylist.data.db.dao.PlaylistDao
import com.example.playlistmaker.creatingPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.data.db.dao.TrackDao
import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.player.data.db.dao.TracksFromPlaylistsDao
import com.example.playlistmaker.player.data.db.entity.TrackFromPlaylistsEntity

@Database(
    version = 4,
    entities = [TrackEntity::class, PlaylistEntity::class, TrackFromPlaylistsEntity::class],
    exportSchema = false)

abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao

    abstract fun getPlaylistDao(): PlaylistDao

    abstract fun getTracksFromPlaylistsDao(): TracksFromPlaylistsDao
}