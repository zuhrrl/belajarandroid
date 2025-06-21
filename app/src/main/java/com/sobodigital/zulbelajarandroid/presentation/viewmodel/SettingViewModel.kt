package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingViewModel (private val pref: SettingPreferences) : ViewModel() {
    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn = _isLoggedIn

    fun getThemeSettings(): LiveData<Any> {
        return pref.getPreferenceSetting(SettingPreferences.THEME_KEY).asLiveData()
    }

    fun checkIsLoggedIn()  {
        viewModelScope.launch {
            val token = pref.getPreferenceSetting(SettingPreferences.AUTH_TOKEN_KEY).first()
            val isLoggedIn = token is String && token.isNotEmpty()
            Log.d(TAG, "Status check login $isLoggedIn")
            Log.d(TAG, "Data check login $token")

            _isLoggedIn.value = isLoggedIn
        }
    }

    fun logoutApp() {
        viewModelScope.launch {
            pref.clearAllPreference()
            checkIsLoggedIn()
        }
    }


    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveSetting(SettingPreferences.THEME_KEY, isDarkModeActive)
        }
    }

    companion object {
        private var TAG = SettingViewModel::class.java.simpleName
    }
}