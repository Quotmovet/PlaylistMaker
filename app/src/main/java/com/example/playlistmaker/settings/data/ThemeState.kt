package com.example.playlistmaker.settings.data

interface ThemeState {
    fun getThemeSettings(): Boolean
    fun updateThemeSettings(state: Boolean)
}