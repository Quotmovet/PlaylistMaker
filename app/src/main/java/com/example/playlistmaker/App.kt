package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.android.ext.android.inject

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(
                dataModule,
                interactorModule,
                repositoryModule,
                viewModelModule))
        }

        val settingsInteractor: SettingsInteractor by inject()
        darkTheme = settingsInteractor.getThemeSettings()
        applyTheme()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        applyTheme()
        val settingsInteractor: SettingsInteractor by inject()
        settingsInteractor.updateThemeSettings(darkThemeEnabled)
    }

    private fun applyTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}