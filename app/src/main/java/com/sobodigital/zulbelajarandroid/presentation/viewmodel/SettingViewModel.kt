package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.domain.usecase.AuthUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.SettingUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingViewModel (private val settingUseCase: SettingUseCase,
    private val authUseCase: AuthUseCase) : ViewModel() {
    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn = _isLoggedIn

    fun getThemeSettings(): LiveData<Any> {
        return settingUseCase.getThemeSettings()
    }

    fun checkIsLoggedIn()  {
        viewModelScope.launch {
            val isLoggedIn = authUseCase.checkIsLoggedIn()
            _isLoggedIn.value = isLoggedIn
        }
    }

    fun logoutApp() {
        viewModelScope.launch {
            settingUseCase.clearAllPreference()
            checkIsLoggedIn()
        }
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            settingUseCase.saveSetting(SettingPreferences.THEME_KEY, isDarkModeActive)
        }
    }

    companion object {
        private var TAG = SettingViewModel::class.java.simpleName
    }
}