package com.sobodigital.zulbelajarandroid.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.sobodigital.zulbelajarandroid.di.Injection

class SettingViewModelFactory (private val pref: SettingPreferences) : NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: SettingViewModelFactory? = null
        fun getInstance(context: Context): SettingViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: SettingViewModelFactory(Injection.providePrefs(context))
            }.also { instance = it }
    }
}