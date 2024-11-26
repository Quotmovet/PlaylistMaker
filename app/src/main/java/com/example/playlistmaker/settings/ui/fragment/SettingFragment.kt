package com.example.playlistmaker.settings.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.viewModel.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {
    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var binding: FragmentSettingsBinding

    private lateinit var themeSwitcher: SwitchMaterial
    private lateinit var buttonShareInApp: MaterialTextView
    private lateinit var buttonGroupSupport: MaterialTextView
    private lateinit var buttonArrowInTermOfUse: MaterialTextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setupListeners()
    }

    // Инициализация UI компонентов
    private fun initViews() {
        themeSwitcher = binding.switcher
        buttonShareInApp = binding.share
        buttonGroupSupport = binding.group
        buttonArrowInTermOfUse = binding.arrow
        themeSwitcher.isChecked = viewModel.getThemeState()
    }

    // Установка слушателей для UI компонентов
    private fun setupListeners() {
        buttonShareInApp.setOnClickListener { shareApp(viewModel.getLinkToCourse()) }
        buttonGroupSupport.setOnClickListener { openEmailSupport(viewModel.getArrayOfEmailAddresses()[0]) }
        buttonArrowInTermOfUse.setOnClickListener { openTermsOfUse(viewModel.getPracticumOffer()) }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.saveAndChangeThemeState(checked)
            (requireContext().applicationContext as App).switchTheme(checked)
        }
    }

    // Поделиться ссылкой
    private fun shareApp(link: String?) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }

    // Открыть группу поддержки
    private fun openEmailSupport(email: String?) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.emailSubject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.emailBody))
        }
        startActivity(intent)
    }

    // Открыть условия использования
    private fun openTermsOfUse(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}