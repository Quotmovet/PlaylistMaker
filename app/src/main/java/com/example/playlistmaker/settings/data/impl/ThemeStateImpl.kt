package com.example.playlistmaker.settings.data.impl

import com.example.playlistmaker.settings.data.ThemeState

class ThemeStateImpl(private val themeState: ThemeState) : ThemeState {
    override fun getThemeSettings(): Boolean = themeState.getThemeSettings()
    override fun updateThemeSettings(state: Boolean) = themeState.updateThemeSettings(state)
}