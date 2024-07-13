package com.example.playlistmaker.settings.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.di.App
import com.example.playlistmaker.settings.ui.viewModel.SettingsViewModel
import com.example.playlistmaker.settings.ui.viewModel.SettingsViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingActivity : AppCompatActivity() {

    private lateinit var themeSwitcher: SwitchMaterial
    private lateinit var buttonShareInApp: MaterialTextView
    private lateinit var buttonGroupSupport: MaterialTextView
    private lateinit var buttonArrowInTermOfUse: MaterialTextView
    private lateinit var viewModel: SettingsViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel = ViewModelProvider(this,
            SettingsViewModelFactory(this))[SettingsViewModel::class.java]

        initViews()
        setupListeners()
    }

    // Инициализация UI компонентов
    private fun initViews() {
        themeSwitcher = findViewById(R.id.switcher)
        buttonShareInApp = findViewById(R.id.share)
        buttonGroupSupport = findViewById(R.id.group)
        buttonArrowInTermOfUse = findViewById(R.id.arrow)
        themeSwitcher.isChecked = viewModel.getThemeState()
    }

    // Настройка слушателей для UI компонентов
    private fun setupListeners() {
        buttonShareInApp.setOnClickListener { shareApp() }
        buttonGroupSupport.setOnClickListener { openEmailSupport() }
        buttonArrowInTermOfUse.setOnClickListener { openTermsOfUse() }

        findViewById<MaterialToolbar>(R.id.main_back_button).setOnClickListener { finish() }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.saveAndChangeThemeState(checked)
            (application as App).switchTheme(checked)
            recreate()
        }
    }

    // Отправка приложения другими пользователями через email
    private fun shareApp() {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, viewModel.getLinkToCourse())
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }

    // Открытие группы поддержки через email
    private fun openEmailSupport() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, viewModel.getArrayOfEmailAddresses())
            putExtra(Intent.EXTRA_SUBJECT, viewModel.getEmailSubject())
            putExtra(Intent.EXTRA_TEXT, viewModel.getEmailMessage())
        }
        startActivity(intent)
    }

    // Открытие ссылки на пользовательское соглашение
    private fun openTermsOfUse() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.getPracticumOffer()))
        startActivity(intent)
    }
}