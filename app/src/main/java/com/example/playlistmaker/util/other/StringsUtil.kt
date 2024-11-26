package com.example.playlistmaker.util.other

class StringsUtil {
    companion object {
        fun countTracks(count: Int): String {
            val string = when (count % 10) {
                1 -> " трек"
                2, 3, 4 -> " трека"
                else -> " треков"
            }
            return count.toString() + string
        }

        fun countMinutes(count: Int): String {
            val string = when (count % 10) {
                1 -> " минута"
                2, 3, 4 -> " минуты"
                else -> " минут"
            }
            return count.toString() + string
        }
    }
}