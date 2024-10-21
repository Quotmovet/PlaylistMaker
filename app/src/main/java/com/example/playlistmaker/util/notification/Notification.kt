package com.example.playlistmaker.util.notification

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.playlistmaker.R
import com.google.android.material.snackbar.Snackbar

class Notification private constructor(private val snackbar: Snackbar) {

    companion object {
        fun make(view: View, message: String, duration: Int = Snackbar.LENGTH_LONG): Notification {
            val snackbar = Snackbar.make(view, "", duration)
            val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

            val inflater = LayoutInflater.from(view.context)
            val customView = inflater.inflate(R.layout.view_element_notification, null)

            customView.findViewById<TextView>(R.id.notification).text = message

            snackbarLayout.setBackgroundColor(Color.TRANSPARENT)

            snackbarLayout.removeAllViews()
            snackbarLayout.addView(customView)

            return Notification(snackbar)
        }
    }

    fun show() {
        snackbar.show()
    }
}