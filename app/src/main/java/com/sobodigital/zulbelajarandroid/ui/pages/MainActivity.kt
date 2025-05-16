package com.sobodigital.zulbelajarandroid.ui.pages

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sobodigital.zulbelajarandroid.R
import com.sobodigital.zulbelajarandroid.databinding.MainActivityBinding
import com.sobodigital.zulbelajarandroid.viewmodel.SettingViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.SettingViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var binding: MainActivityBinding

    companion object{
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNav = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val factory: SettingViewModelFactory = SettingViewModelFactory.getInstance(this)
        val viewModel: SettingViewModel by viewModels { factory }

        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
//            if (isDarkModeActive) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//
//            }
        }

        bottomNav.setupWithNavController(navController)

    }

}