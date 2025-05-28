package com.sobodigital.zulbelajarandroid.viewmodel

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel (private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<Any> {
        return pref.getPreferenceSetting(SettingPreferences.THEME_KEY).asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(SettingPreferences.THEME_KEY, isDarkModeActive)
        }
    }
}