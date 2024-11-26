package com.example.playlistmaker.sharing.domain.repository

interface SharingStateRepository {
    fun getStringFromStorage(id: Int): String
}