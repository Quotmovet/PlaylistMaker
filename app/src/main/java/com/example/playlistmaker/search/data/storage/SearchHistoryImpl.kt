package com.example.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.datasource.SearchHistoryLocalDataSource
import com.example.playlistmaker.search.data.dto.TrackDataClassDto
import com.example.playlistmaker.util.Constatn.KEY_FOR_HISTORY_LIST
import com.google.gson.Gson

class SearchHistoryImpl(private val sharedPreferences: SharedPreferences
) : SearchHistoryLocalDataSource {

    override fun getTracksFromHistory(): Array<TrackDataClassDto> {
        val json = sharedPreferences
            .getString(KEY_FOR_HISTORY_LIST, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<TrackDataClassDto>::class.java)
    }

    override fun saveTracksToHistory(tracks: ArrayList<TrackDataClassDto>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(KEY_FOR_HISTORY_LIST, json)
            .apply()
    }
}
