package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {
    private val message by lazy { resources.getString(R.string.share_app) }
    private val emailSubject by lazy { resources.getString(R.string.emailSubject) }
    private val emailBody by lazy { resources.getString(R.string.emailBody) }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Возврат назад
        val buttonBackToMain = findViewById<ImageButton>(R.id.ic_arrow_back)

        buttonBackToMain.setOnClickListener{
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
            finish()
        }

        // Поделиться
        val buttonShareInApp = findViewById<ImageButton>(R.id.share)

        buttonShareInApp.setOnClickListener{
            val linkOnCourse = "https://practicum.yandex.ru/"
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SENDTO
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, linkOnCourse)

            startActivity(Intent.createChooser(shareIntent, message))
        }

        // Написать в тех. поддержку
        val buttonGroupSupport = findViewById<ImageButton>(R.id.group)

        buttonGroupSupport.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("student_email@domain.com"))
                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                putExtra(Intent.EXTRA_TEXT, emailBody)
            }

            startActivity(emailIntent)
        }

        // Открыть пользовательское соглашение
        val buttonArrowInTermOfUse = findViewById<ImageButton>(R.id.arrow)

        buttonArrowInTermOfUse.setOnClickListener{
            val linkOfTerms = "https://yandex.ru/legal/practicum_offer/"
            val openTerms = Intent(Intent.ACTION_VIEW, Uri.parse(linkOfTerms))

            startActivity(openTerms)
        }
    }
}