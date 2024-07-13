package com.example.playlistmaker.settings.ui.viewModelimport android.app.Applicationimport androidx.lifecycle.AndroidViewModelimport com.example.playlistmaker.Rimport com.example.playlistmaker.settings.domain.SettingsInteractorimport com.example.playlistmaker.sharing.domain.SharingInteractorclass SettingsViewModel(    private val application: Application,    private val settingsInteractor: SettingsInteractor,    private val sharingInteractor: SharingInteractor) : AndroidViewModel(application) {    // Взаимодействие с интерактором и получение настроек темы    fun getThemeState(): Boolean = settingsInteractor.getThemeSettings()    // Взаимодействие с интерактором и сохранение настроек темы    fun saveAndChangeThemeState(state: Boolean) {        settingsInteractor.updateThemeSettings(state)    }    // Взаимодействие с интерактором и получение данных    fun getLinkToCourse(): String = sharingInteractor.getString(R.string.linkOnCourse)    fun getEmailSubject(): String = sharingInteractor.getString(R.string.emailSubject)    fun getEmailMessage(): String = sharingInteractor.getString(R.string.emailBody)    fun getPracticumOffer(): String = sharingInteractor.getString(R.string.linkOfTerms)    fun getArrayOfEmailAddresses(): Array<String> = arrayOf(sharingInteractor.getString(R.string.email))}