package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val buttonBackToMain = findViewById<ImageButton>(R.id.ic_arrow_back)

        buttonBackToMain.setOnClickListener{
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }
    }
}