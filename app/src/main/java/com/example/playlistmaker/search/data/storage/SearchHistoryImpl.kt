package com.example.playlistmaker.search.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.search.data.dto.TrackDataClassDto
import com.example.playlistmaker.util.Constatn.KEY_FOR_HISTORY_LIST
import com.example.playlistmaker.util.Constatn.SHARED_PREFERENCES
import com.google.gson.Gson

class SearchHistoryImpl (context: Context): SearchHistoryRepository {

    private val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
        SHARED_PREFERENCES, Context.MODE_PRIVATE)

    override fun getTracksFromHistory(): Array<TrackDataClassDto> {
        val json = sharedPreferences?.getString(KEY_FOR_HISTORY_LIST, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<TrackDataClassDto>::class.java)
    }

    override fun saveTracksToHistory(tracks: ArrayList<TrackDataClassDto>) {
        val json = Gson().toJson(tracks)
        sharedPreferences?.edit()
            ?.putString(KEY_FOR_HISTORY_LIST, json)
            ?.apply()
    }
}