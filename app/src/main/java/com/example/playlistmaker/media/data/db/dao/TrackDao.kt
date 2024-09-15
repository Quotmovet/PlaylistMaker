package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritesTracks(tracks: List<TrackEntity>)

    @Query("DELETE FROM favorite_tracks_table WHERE trackId = :trackId")
    suspend fun deleteFavoriteTrackById(trackId: Int)

    @Query("SELECT * FROM favorite_tracks_table ORDER BY id DESC")
    suspend fun getFavoritesTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM favorite_tracks_table")
    suspend fun getFavoriteTracksIds(): List<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_tracks_table WHERE trackId = :trackId)")
    suspend fun isTrackFavorite(trackId: Int): Boolean
}