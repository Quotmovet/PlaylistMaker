package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.data.ThemeState
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsInteractorImpl(private val themeState: ThemeState) : SettingsInteractor {
    override fun getThemeSettings(): Boolean = themeState.getThemeSettings()
    override fun updateThemeSettings(theme: Boolean) = themeState.updateThemeSettings(theme)
}