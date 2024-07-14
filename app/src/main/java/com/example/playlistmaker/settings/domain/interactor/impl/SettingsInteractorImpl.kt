package com.example.playlistmaker.settings.domain.interactor.impl

import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.example.playlistmaker.settings.domain.repository.SettingsRepository

class SettingsInteractorImpl(private val themeState: SettingsRepository) : SettingsInteractor {
    override fun getThemeSettings(): Boolean = themeState.getThemeSettings()
    override fun updateThemeSettings(theme: Boolean) = themeState.updateThemeSettings(theme)
}