package com.example.playlistmaker.util.extensions

import android.content.Context
import kotlin.math.roundToInt

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).roundToInt()
}

fun Context.dpToPx(dp: Float): Int {
    return (dp * resources.displayMetrics.density).roundToInt()
}