package com.example.playlistmaker.media.domain.interactor.impl

import com.example.playlistmaker.media.domain.db.FavoritesRepository
import com.example.playlistmaker.media.domain.interactor.FavoriteTracksInteractor
import com.example.playlistmaker.search.domain.model.TrackDataClass
import kotlinx.coroutines.flow.Flow

class FavoritesTracksInteractorImpl (
    private val favoritesRepository: FavoritesRepository) : FavoriteTracksInteractor {

    override fun getFavoriteTracks(): Flow<List<TrackDataClass>> {
        return favoritesRepository.getTracks()
    }
}
