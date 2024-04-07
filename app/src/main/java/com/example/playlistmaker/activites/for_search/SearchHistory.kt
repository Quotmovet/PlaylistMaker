package com.example.playlistmaker.activites.for_search

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory (private val sharedPreferences: SharedPreferences) {

    private val jsonString = sharedPreferences.getString(HISTORY_OF_SEARCH_KEY, null)
    private val tracksList: ArrayList<TrackDataClass> = if (jsonString.isNullOrEmpty()) {
        ArrayList()
    } else {
        Gson().fromJson(jsonString, object : TypeToken<ArrayList<TrackDataClass>>() {}.type)
    }

    fun show(): List<TrackDataClass>{
        return tracksList.reversed()
    }

    fun saveHistory(track: TrackDataClass) {

        if (tracksList.any { it.trackId == track.trackId }) {
            tracksList.removeIf { it.trackId == track.trackId }
        }
        tracksList.add(track)

        if (tracksList.size > HISTORY_SIZE) {
            tracksList.removeAt(0)
        }

        val json = Gson().toJson(tracksList)
        sharedPreferences.edit {
            if (tracksList.isEmpty()) {
                remove(HISTORY_OF_SEARCH_KEY)
            } else {
                putString(HISTORY_OF_SEARCH_KEY, json)
            }
        }
    }

    fun cleanHistory() {
        tracksList.clear()
        sharedPreferences.edit {
            remove(HISTORY_OF_SEARCH_KEY)
        }
    }

    companion object{
        private const val HISTORY_OF_SEARCH_KEY = "history_of_search_key"
        private const val HISTORY_SIZE = 10
    }
}