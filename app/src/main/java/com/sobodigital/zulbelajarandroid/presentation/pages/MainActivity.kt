package com.sobodigital.zulbelajarandroid.presentation.pages

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sobodigital.zulbelajarandroid.R
import com.sobodigital.zulbelajarandroid.databinding.MainActivityBinding
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.SettingViewModel
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.SettingViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var binding: MainActivityBinding

    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        bottomNav = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val factory: SettingViewModelFactory = SettingViewModelFactory.getInstance(this)
        val viewModel: SettingViewModel by viewModels { factory }

        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Any ->
            if (isDarkModeActive as Boolean) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                return@observe
            }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        bottomNav.setupWithNavController(navController)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }


    }

}