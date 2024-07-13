package com.example.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.media.ui.MediaActivity
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.example.playlistmaker.settings.ui.activity.SettingActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMedia = findViewById<Button>(R.id.button_media)
        val buttonSetting = findViewById<Button>(R.id.button_settings)

        val searchButtonClickListener: View.OnClickListener = View.OnClickListener {
            val displayIntent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(displayIntent)
        }
        buttonSearch.setOnClickListener(searchButtonClickListener)

        buttonMedia.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(displayIntent)
        }

        buttonSetting.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(displayIntent)
        }
    }
}