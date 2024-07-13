package com.example.playlistmaker.di

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        darkTheme = getThemeFromPreferences()
        applyTheme()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        applyTheme()
        saveThemeToPreferences(darkThemeEnabled)
    }

    private fun applyTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun getThemeFromPreferences(): Boolean {
        val themeState = Creator.provideThemeStateInteractor(this)
        return themeState.getThemeSettings()
    }

    private fun saveThemeToPreferences(isDarkTheme: Boolean) {
        val themeState = Creator.provideThemeStateInteractor(this)
        themeState.updateThemeSettings(isDarkTheme)
    }
}