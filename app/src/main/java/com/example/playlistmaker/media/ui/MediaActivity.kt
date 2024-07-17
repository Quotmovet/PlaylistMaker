package com.example.playlistmaker.media.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.google.android.material.appbar.MaterialToolbar

class MediaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val toolbar: MaterialToolbar = findViewById(R.id.main_back_button)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}