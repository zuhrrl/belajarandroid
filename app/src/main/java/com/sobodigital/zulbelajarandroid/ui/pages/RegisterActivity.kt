package com.sobodigital.zulbelajarandroid.ui.pages

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sobodigital.zulbelajarandroid.data.model.RegisterParameter
import com.sobodigital.zulbelajarandroid.databinding.ActivityRegisterBinding
import com.sobodigital.zulbelajarandroid.utils.navigateHome
import com.sobodigital.zulbelajarandroid.viewmodel.AuthViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.AuthViewModelFactory

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

        binding.btnRegister.setOnClickListener {
            val param = RegisterParameter(
                name = "Zul Coba coba",
                email = "zulemailtest1@gmail.com",
                password = "zulemailtest1@gmail.com")
            viewModel.register(param)
        }

    }

    companion object {
        private var TAG = RegisterActivity::class.java.simpleName
    }
}