package com.example.playlistmaker.sharing.domain.interactor.impl

import com.example.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.example.playlistmaker.sharing.domain.repository.SharingState

class SharingInteractorImpl(private val sharingState: SharingState) : SharingInteractor {
    override fun getString(id: Int): String = sharingState.getStringFromStorage(id)
}