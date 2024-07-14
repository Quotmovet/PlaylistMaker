package com.example.playlistmaker.settings.domain.interactor

interface SettingsInteractor {
    fun getThemeSettings(): Boolean
    fun updateThemeSettings(theme: Boolean)
}