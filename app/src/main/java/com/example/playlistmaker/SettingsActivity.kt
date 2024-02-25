package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {
    private val message by lazy { resources.getString(R.string.share_app) }
    private val emailSubject by lazy { resources.getString(R.string.emailSubject) }
    private val emailBody by lazy { resources.getString(R.string.emailBody) }
    private val linkOnCourse by lazy { resources.getString(R.string.linkOnCourse) }
    private val linkOfTerms by lazy { resources.getString(R.string.linkOfTerms) }
    private val email by lazy { resources.getString(R.string.email) }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Возврат назад
        val toolbar: MaterialToolbar = findViewById(R.id.main_back_button)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Поделиться
        val buttonShareInApp: MaterialTextView = findViewById(R.id.share)

        buttonShareInApp.setOnClickListener{
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, linkOnCourse)

            startActivity(Intent.createChooser(shareIntent, message))
        }

        // Написать в тех. поддержку
        val buttonGroupSupport: MaterialTextView = findViewById(R.id.group)

        buttonGroupSupport.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                putExtra(Intent.EXTRA_TEXT, emailBody)
            }

            startActivity(emailIntent)
        }

        // Открыть пользовательское соглашение
        val buttonArrowInTermOfUse: MaterialTextView = findViewById(R.id.arrow)

        buttonArrowInTermOfUse.setOnClickListener{
            val openTerms = Intent(Intent.ACTION_VIEW, Uri.parse(linkOfTerms))

            startActivity(openTerms)
        }
    }
}