package com.example.playlistmaker.util.other

import java.text.SimpleDateFormat
import java.util.Locale

object TimeUtils {

    private val timeFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())

    fun formatTime(timeMillis: Long): String {
        return timeFormatter.format(timeMillis)
    }
}