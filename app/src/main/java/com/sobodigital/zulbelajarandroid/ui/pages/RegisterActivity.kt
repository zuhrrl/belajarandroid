package com.sobodigital.zulbelajarandroid.ui.pages

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.sobodigital.zulbelajarandroid.data.model.RegisterParameter
import com.sobodigital.zulbelajarandroid.databinding.ActivityRegisterBinding
import com.sobodigital.zulbelajarandroid.utils.navigateHome
import com.sobodigital.zulbelajarandroid.utils.navigateToLogin
import com.sobodigital.zulbelajarandroid.viewmodel.AuthViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.AuthViewModelFactory
import com.sobodigital.zulbelajarandroid.viewmodel.SettingViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.SettingViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: AuthViewModelFactory = AuthViewModelFactory.getInstance(this)
        val viewModel: AuthViewModel by viewModels { factory }

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

        viewModel.registerResponse.observe(this) {data ->
            if(data.error!!) {
                return@observe
            }
            Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
            navigateToLogin(this)
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.fullName.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val param = RegisterParameter(
                name = name,
                email = email,
                password = password)
            viewModel.register(param)
        }

    }

    companion object {
        private var TAG = RegisterActivity::class.java.simpleName
    }
}