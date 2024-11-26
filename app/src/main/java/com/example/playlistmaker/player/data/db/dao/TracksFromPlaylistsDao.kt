package com.example.playlistmaker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.player.data.db.entity.TrackFromPlaylistsEntity

@Dao
interface TracksFromPlaylistsDao {

    // Добавление трека в таблицу 'tracks_from_playlists_table'
    @Insert(entity = TrackFromPlaylistsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackFromPlaylistsEntity)
}