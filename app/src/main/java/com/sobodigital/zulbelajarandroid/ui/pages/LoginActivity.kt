package com.sobodigital.zulbelajarandroid.ui.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.data.model.AuthParameter
import com.sobodigital.zulbelajarandroid.databinding.ActivityLoginBinding
import com.sobodigital.zulbelajarandroid.di.Injection
import com.sobodigital.zulbelajarandroid.utils.navigateHome
import com.sobodigital.zulbelajarandroid.viewmodel.AuthViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.AuthViewModelFactory
import com.sobodigital.zulbelajarandroid.viewmodel.SettingViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.SettingViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: AuthViewModelFactory = AuthViewModelFactory.getInstance(this)
        val viewModel: AuthViewModel by viewModels { factory }

        val settingFactory: SettingViewModelFactory = SettingViewModelFactory.getInstance(this)
        val settingViewModel: SettingViewModel by viewModels { settingFactory }

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
            val param = AuthParameter(email = "zulemailtest@gmail.com",
                password = "zulemailtest@gmail.com")
            viewModel.authWithEmail(param)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    companion object {
        private var TAG = LoginActivity::class.java.simpleName
    }
}