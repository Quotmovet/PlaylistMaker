package com.example.playlistmaker.sharing.domain.repository

interface SharingState {
    fun getStringFromStorage(id: Int): String
}