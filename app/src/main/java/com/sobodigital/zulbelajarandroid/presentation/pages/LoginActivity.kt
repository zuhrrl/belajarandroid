package com.sobodigital.zulbelajarandroid.presentation.pages

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sobodigital.zulbelajarandroid.R
import com.sobodigital.zulbelajarandroid.domain.model.AuthCredential
import com.sobodigital.zulbelajarandroid.databinding.ActivityLoginBinding
import com.sobodigital.zulbelajarandroid.utils.navigateHome
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.AuthViewModel
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.AuthViewModelFactory
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.SettingViewModel
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.SettingViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: AuthViewModelFactory = AuthViewModelFactory.getInstance(this)
        val viewModel: AuthViewModel by viewModels { factory }

        val settingFactory: SettingViewModelFactory = SettingViewModelFactory.getInstance(this)
        val settingViewModel: SettingViewModel by viewModels { settingFactory }

        val factorySetting: SettingViewModelFactory = SettingViewModelFactory.getInstance(this)
        val viewModelSetting: SettingViewModel by viewModels { factorySetting }

        viewModelSetting.getThemeSettings().observe(this) { isDarkModeActive: Any ->
            if (isDarkModeActive as Boolean) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                return@observe
            }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        settingViewModel.checkIsLoggedIn()
        settingViewModel.isLoggedIn.observe(this) {isLoggedIn ->
            Log.d(TAG,"status $isLoggedIn")
            if(isLoggedIn) {
                navigateHome(this)
                return@observe
            }
        }

        viewModel.isLoading.observe(this) {isLoading ->
            if(isLoading) {
                binding.loading.visibility = View.VISIBLE
                return@observe
            }
            binding.loading.visibility = View.GONE
            return@observe
        }

        viewModel.errorMessage.observe(this) {message ->
            Log.d(TAG, message)
            if(message.isNotEmpty()) {
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorMessage.text = message
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return@observe
            }
            binding.errorMessage.visibility = View.GONE
            return@observe
        }

        viewModel.authResponse.observe(this) {data ->
            if(data.error!!) {
                return@observe
            }
            navigateHome(this)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val param = AuthCredential(email = email,
                password = password)
            viewModel.authWithEmail(param)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        val slideRight = ValueAnimator.ofFloat(0f, 10f).apply {
            duration = 1500
            addUpdateListener { updatedAnimation ->
                binding.scrollView.translationX = updatedAnimation.animatedValue as Float
            }
        }

        val slideLeft = ValueAnimator.ofFloat(10f, 1f).apply {
            duration = 1000
            addUpdateListener { updatedAnimation ->
                binding.scrollView.translationX = updatedAnimation.animatedValue as Float
            }
        }

        AnimatorSet().apply {
            play(slideRight).before(slideLeft)
            start()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    companion object {
        private var TAG = LoginActivity::class.java.simpleName
    }
}