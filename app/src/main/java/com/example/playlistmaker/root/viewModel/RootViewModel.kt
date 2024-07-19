package com.example.playlistmaker.root.viewModel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.interactor.SettingsInteractor

class RootViewModel (
    private val settingsInteractor: SettingsInteractor,
) : ViewModel(){

    fun getAppTheme () : Boolean =  settingsInteractor.getThemeSettings()
}