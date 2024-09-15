package com.example.playlistmaker.media.data.db.converters

import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.model.TrackDataClass

class TrackDbConverter {
    fun map(track: TrackDataClass): TrackEntity {
        return TrackEntity(
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

    fun map(track: TrackEntity): TrackDataClass {
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