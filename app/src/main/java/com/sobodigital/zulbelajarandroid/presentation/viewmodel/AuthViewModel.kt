package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.model.AuthCredential
import com.sobodigital.zulbelajarandroid.domain.model.AuthSession
import com.sobodigital.zulbelajarandroid.domain.model.RegisterData
import com.sobodigital.zulbelajarandroid.domain.model.RegisterSession
import com.sobodigital.zulbelajarandroid.domain.usecase.AuthUseCase
import kotlinx.coroutines.launch

class AuthViewModel(private val authUsecase: AuthUseCase) : ViewModel() {
    private val _authSession = MutableLiveData<AuthSession>()
    val authSession = _authSession

    private val _registerSession = MutableLiveData<RegisterSession>()
    val registerSession = _registerSession

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun authWithEmail(authCredential: AuthCredential) {
        viewModelScope.launch {
            _errorMessage.value = ""
            _isLoading.value = true
            when(val response = authUsecase.authWithEmail(authCredential)) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: ${response.error}"
                }

                is Result.Success -> {
                    _isLoading.value = false
                    _authSession.value = response.data
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
                    _registerSession.value = response.data
                }

                null -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: Unimplemented!"

                }
            }
        }
    }
}