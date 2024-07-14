package com.example.playlistmaker.settings.data.repository

import com.example.playlistmaker.settings.domain.repository.ThemeState

class ThemeStateImpl(private val themeState: ThemeState) : ThemeState {
    override fun getThemeSettings(): Boolean = themeState.getThemeSettings()
    override fun updateThemeSettings(state: Boolean) = themeState.updateThemeSettings(state)
}