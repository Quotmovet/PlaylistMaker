package com.example.playlistmaker.sharing.data

import android.content.Context

class SharingStateResource(private val context: Context) : SharingState {
    override fun getStringFromStorage(id: Int): String {
        return context.getString(id)
    }
}