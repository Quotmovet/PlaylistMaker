package com.example.playlistmaker.settings.data.repository

import com.example.playlistmaker.settings.domain.repository.SettingsRepository
import android.content.SharedPreferences

class SettingsRepositoryImplSP(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    companion object {
        private const val SWITCHER_KEY = "switcher_key"
    }

    override fun getThemeSettings(): Boolean {
        return sharedPreferences.getBoolean(SWITCHER_KEY, false)
    }

    override fun updateThemeSettings(state: Boolean) {
        sharedPreferences.edit()
            .putBoolean(SWITCHER_KEY, state)
            .apply()
    }
}