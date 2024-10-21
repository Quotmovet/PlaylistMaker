package com.example.playlistmaker.root.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.root.viewModel.RootViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private var bottomNavigationViewVisibility = View.VISIBLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModel<RootViewModel>()

        (applicationContext as App).switchTheme(viewModel.getAppTheme())

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Устанавливаем фрагмент для навигации
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

        // Устанавливаем поведение навигации
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.newPlaylistFragment) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            } else {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!navController.popBackStack()) {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        // Обработка изменения местоположения в навигации
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.e("МОЁ", "destination = " + destination.id.toString())
            when (destination.id) {
                R.id.newPlaylistFragment -> {
                    bottomNavigationView.visibility = View.GONE
                    bottomNavigationViewVisibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                    bottomNavigationViewVisibility = View.VISIBLE
                }
            }
        }
    }
}