package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchTrackRepository
import com.example.playlistmaker.domain.api.TrackInteractor
import java.util.concurrent.Executors

class SearchTracksInteractorImpl(private val repository: SearchTrackRepository):
    TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchTrack(expression))
        }
    }
}