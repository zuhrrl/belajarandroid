package com.sobodigital.zulbelajarandroid.viewmodel

import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.ui.pages.RegisterActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingViewModel (private val pref: SettingPreferences) : ViewModel() {
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn = _isLoggedIn

    fun getThemeSettings(): LiveData<Any> {
        return pref.getPreferenceSetting(SettingPreferences.THEME_KEY).asLiveData()
    }

    fun checkIsLoggedIn()  {
        viewModelScope.launch {
            val token = pref.getPreferenceSetting(SettingPreferences.AUTH_TOKEN_KEY).first()
            val isLoggedIn = token is String && token.isNotEmpty()
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