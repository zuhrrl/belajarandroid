package com.sobodigital.zulbelajarandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.model.AuthParameter
import com.sobodigital.zulbelajarandroid.data.model.LoginResponse
import com.sobodigital.zulbelajarandroid.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _authResponse = MutableLiveData<LoginResponse>()
    val authResponse = _authResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun authWithEmail(param: AuthParameter) {
        viewModelScope.launch {
            _errorMessage.value = ""
            _isLoading.value = true
            when(val response = repository.authWithEmail(param)) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: ${response.error}"
                }
                Result.Loading -> {
                    _isLoading.value = true
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
}