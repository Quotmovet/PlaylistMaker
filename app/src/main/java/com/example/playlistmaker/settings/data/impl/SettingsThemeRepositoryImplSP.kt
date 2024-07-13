package com.example.playlistmaker.settings.data.impl

import com.example.playlistmaker.settings.data.ThemeState
import android.content.SharedPreferences

class SettingsThemeRepositoryImplSP(
    private val sharedPreferences: SharedPreferences
) : ThemeState {

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