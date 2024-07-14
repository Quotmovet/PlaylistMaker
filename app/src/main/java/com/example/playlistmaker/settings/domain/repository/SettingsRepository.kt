package com.example.playlistmaker.settings.domain.repository

interface SettingsRepository {
    fun getThemeSettings(): Boolean
    fun updateThemeSettings(state: Boolean)
}