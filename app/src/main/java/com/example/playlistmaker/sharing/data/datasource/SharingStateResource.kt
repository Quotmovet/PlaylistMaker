package com.example.playlistmaker.sharing.data.datasource

import android.content.Context
import com.example.playlistmaker.sharing.domain.repository.SharingState

class SharingStateResource(private val context: Context) : SharingState {
    override fun getStringFromStorage(id: Int): String {
        return context.getString(id)
    }
}