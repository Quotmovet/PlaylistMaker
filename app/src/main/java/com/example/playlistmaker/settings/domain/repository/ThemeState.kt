package com.example.playlistmaker.settings.domain.repository

interface ThemeState {
    fun getThemeSettings(): Boolean
    fun updateThemeSettings(state: Boolean)
}