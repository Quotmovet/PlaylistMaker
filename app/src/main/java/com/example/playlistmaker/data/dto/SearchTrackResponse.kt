package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.TrackDataClass

class SearchTrackResponse (val results: List<TrackDataClass>): Response()