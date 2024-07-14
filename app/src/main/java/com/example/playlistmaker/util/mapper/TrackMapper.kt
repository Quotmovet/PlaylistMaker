package com.example.playlistmaker.util.mapper

import com.example.playlistmaker.search.data.dto.TrackDataClassDto
import com.example.playlistmaker.search.domain.model.TrackDataClass

object TrackMapper {
    fun mapTrackDataToDomain(trackDatadto: TrackDataClassDto): TrackDataClass {
        return TrackDataClass(
            trackName = trackDatadto.trackName,
            artistName = trackDatadto.artistName,
            trackTimeMillis = trackDatadto.trackTimeMillis,
            artworkUrl1100 = trackDatadto.artworkUrl1100,
            collectionName = trackDatadto.collectionName,
            releaseDate = trackDatadto.releaseDate,
            primaryGenreName = trackDatadto.primaryGenreName,
            country = trackDatadto.country,
            previewUrl = trackDatadto.previewUrl,
            trackId = trackDatadto.trackId
        )
    }

    fun mapTrackDomainToUi(trackData: TrackDataClass): TrackDataClass {
        return TrackDataClass(
            trackName = trackData.trackName,
            artistName = trackData.artistName,
            trackTimeMillis = trackData.trackTimeMillis,
            artworkUrl1100 = trackData.artworkUrl1100,
            collectionName = trackData.collectionName,
            releaseDate = trackData.releaseDate,
            primaryGenreName = trackData.primaryGenreName,
            country = trackData.country,
            previewUrl = trackData.previewUrl,
            trackId = trackData.trackId
        )
    }
}