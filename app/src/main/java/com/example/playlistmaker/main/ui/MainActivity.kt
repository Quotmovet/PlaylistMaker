package com.example.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.main.model.MainState
import com.example.playlistmaker.main.viewmodel.MainViewModel
import com.example.playlistmaker.media.ui.MediaActivity
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.example.playlistmaker.settings.ui.activity.SettingActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModel<MainViewModel>()

        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMedia = findViewById<Button>(R.id.button_media)
        val buttonSetting = findViewById<Button>(R.id.button_settings)

        buttonSearch.setOnClickListener {
            viewModel.clickSearch()
        }

        buttonMedia.setOnClickListener {
            viewModel.clickMediateca()
        }

        buttonSetting.setOnClickListener {
            viewModel.clickSettings()
        }

        viewModel.getStateLiveData().observe(this) { state ->
            when(state) {
                MainState.SEARCH -> {
                    val intent = Intent(this@MainActivity, SearchActivity::class.java)
                    startActivity(intent)
                }
                MainState.MEDIA -> {
                    val intent = Intent(this@MainActivity, MediaActivity::class.java)
                    startActivity(intent)
                }
                MainState.SETTINGS -> {
                    val intent = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivity(intent)
                }
                MainState.NOTHING -> {}
            }
            viewModel.nothing()
        }
    }
}