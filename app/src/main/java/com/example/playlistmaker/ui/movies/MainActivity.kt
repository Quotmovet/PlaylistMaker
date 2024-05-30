package com.example.playlistmaker.ui.movies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMedia = findViewById<Button>(R.id.button_media)
        val buttonSetting = findViewById<Button>(R.id.button_settings)

        buttonSearch.setOnClickListener{
            val displayIntent =Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        buttonMedia.setOnClickListener{
            val displayIntent =Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }

        buttonSetting.setOnClickListener{
            val displayIntent =Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}