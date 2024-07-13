package com.example.playlistmaker.settings.ui.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.di.Creator

class SettingsViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val themeStateInteractor = Creator.provideThemeStateInteractor(context)
    private val stringStorageInteractor = Creator.provideStringStorageInteractor(context)
    private val application = context.applicationContext as Application

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            application,
            themeStateInteractor,
            stringStorageInteractor) as T
    }
}