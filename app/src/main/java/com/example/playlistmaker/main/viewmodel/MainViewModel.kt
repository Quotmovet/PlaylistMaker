package com.example.playlistmaker.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.main.model.MainState

class MainViewModel: ViewModel()  {

    private val state: MainState = MainState.NOTHING
    private var stateLiveData = MutableLiveData(state)

    fun getStateLiveData(): LiveData<MainState> = stateLiveData

    fun clickSearch () {
        stateLiveData.postValue(MainState.SEARCH)
    }

    fun clickMediateca () {
        stateLiveData.postValue(MainState.MEDIA)
    }

    fun clickSettings () {
        stateLiveData.postValue(MainState.SETTINGS)
    }

    fun nothing () {
        stateLiveData.postValue(MainState.NOTHING)
    }
}