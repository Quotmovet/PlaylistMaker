package com.example.playlistmaker.creatingPlaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.playlistmaker.creatingPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.player.data.db.entity.TrackFromPlaylistsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(track: PlaylistEntity): Long

    @Update(entity = PlaylistEntity::class)
    fun updatePlaylist(playlist: PlaylistEntity): Int

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Int): PlaylistEntity

    // Возвращает список всех треков из таблицы 'tracks_from_playlists_table'
    @Query("SELECT * FROM tracks_from_playlists_table WHERE trackId IN (:trackIds)")
    fun getTracksFromPlaylists(trackIds: List<Int>): List<TrackFromPlaylistsEntity>

    // Удаление трека по trackId из таблицы 'tracks_from_playlists_table'
    @Query("DELETE FROM tracks_from_playlists_table WHERE trackId = :trackId")
    fun deleteTrackById (trackId: Int)

    // Удаление трека по trackId из таблицы 'playlist_table'
    @Query("DELETE FROM playlist_table WHERE tracksListId = :trackId")
    fun deleteTrackFromPlaylist(trackId: Int)

    // Удаление плейлиста по id из таблицы 'playlist_table'
    @Query("DELETE FROM playlist_table WHERE id = :playlistId")
    fun deletePlaylistById (playlistId: Int)

    @Transaction
    fun deleteTrackCompletely(trackId: Int) {
        deleteTrackFromPlaylist(trackId)
        deleteTrackById(trackId)
    }
}