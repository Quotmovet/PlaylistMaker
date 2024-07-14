package com.example.playlistmaker.sharing.data.datasource

import android.content.Context
import com.example.playlistmaker.sharing.domain.repository.SharingStateRepository

class SharingStateRepositoryImpl(private val context: Context) : SharingStateRepository {
    override fun getStringFromStorage(id: Int): String {
        return context.getString(id)
    }
}