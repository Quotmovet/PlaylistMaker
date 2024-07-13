package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.data.SharingState
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl(private val sharingState: SharingState) : SharingInteractor {
    override fun getString(id: Int): String = sharingState.getStringFromStorage(id)
}