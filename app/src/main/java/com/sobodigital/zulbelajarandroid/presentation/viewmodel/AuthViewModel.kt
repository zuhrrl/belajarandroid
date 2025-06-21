package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.model.AuthCredential
import com.sobodigital.zulbelajarandroid.data.model.RegisterParameter
import com.sobodigital.zulbelajarandroid.data.model.RegisterResponse
import com.sobodigital.zulbelajarandroid.data.repository.AuthRepositoryImpl
import com.sobodigital.zulbelajarandroid.domain.model.AuthSession
import com.sobodigital.zulbelajarandroid.domain.model.RegisterData
import com.sobodigital.zulbelajarandroid.domain.model.RegisterSession
import com.sobodigital.zulbelajarandroid.domain.usecase.AuthUsecase
import kotlinx.coroutines.launch

class AuthViewModel(private val authUsecase: AuthUsecase) : ViewModel() {
    private val _authResponse = MutableLiveData<AuthSession>()
    val authResponse = _authResponse

    private val _registerResponse = MutableLiveData<RegisterSession>()
    val registerResponse = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun authWithEmail(param: AuthCredential) {
        viewModelScope.launch {
            _errorMessage.value = ""
            _isLoading.value = true
            when(val response = authUsecase.authWithEmail(param)) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: ${response.error}"
                }

                is Result.Success -> {
                    _isLoading.value = false
                    _authResponse.value = response.data
                }

                null -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: Unimplemented!"

                }
            }
        }
    }

    fun register(registerData: RegisterData) {
        viewModelScope.launch {
            _errorMessage.value = ""
            _isLoading.value = true
            when(val response = authUsecase.register(registerData)) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: ${response.error}"
                }

                is Result.Success -> {
                    _isLoading.value = false
                    _registerResponse.value = response.data
                }

                null -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: Unimplemented!"

                }
            }
        }
    }
}