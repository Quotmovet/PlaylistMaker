package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBackToMain = findViewById<ImageButton>(R.id.ic_arrow_back)

        buttonBackToMain.setOnClickListener{
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }
    }
}