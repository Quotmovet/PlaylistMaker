package com.example.playlistmaker.player.data.db.converter

import com.example.playlistmaker.player.data.db.entity.TrackFromPlaylistsEntity
import com.example.playlistmaker.search.domain.model.TrackDataClass

class TrackFromPlaylistsEntityDbConvertor {
    fun map(track: TrackDataClass): TrackFromPlaylistsEntity {
        return TrackFromPlaylistsEntity(
            0,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl1100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.trackId
        )
    }

    fun map(track: TrackFromPlaylistsEntity): TrackDataClass {
        return TrackDataClass(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl1100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.trackId
        )
    }
}