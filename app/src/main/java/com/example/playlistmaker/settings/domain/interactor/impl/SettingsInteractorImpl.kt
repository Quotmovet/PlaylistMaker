package com.example.playlistmaker.settings.domain.interactor.impl

import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.settings.domain.repository.ThemeState

class SettingsInteractorImpl(private val themeState: ThemeState) : SettingsInteractor {
    override fun getThemeSettings(): Boolean = themeState.getThemeSettings()
    override fun updateThemeSettings(theme: Boolean) = themeState.updateThemeSettings(theme)
}