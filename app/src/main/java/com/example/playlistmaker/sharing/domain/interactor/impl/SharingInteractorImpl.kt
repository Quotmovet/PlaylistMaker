package com.example.playlistmaker.sharing.domain.interactor.impl

import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.example.playlistmaker.sharing.domain.repository.SharingStateRepository

class SharingInteractorImpl(private val sharingState: SharingStateRepository) : SharingInteractor {
    override fun getString(id: Int): String = sharingState.getStringFromStorage(id)
}