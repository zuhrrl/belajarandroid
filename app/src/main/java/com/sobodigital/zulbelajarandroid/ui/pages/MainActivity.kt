package com.sobodigital.zulbelajarandroid.ui.pages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sobodigital.zulbelajarandroid.R
import com.sobodigital.zulbelajarandroid.databinding.MainActivityBinding


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

        bottomNav.setupWithNavController(navController)

    }

}