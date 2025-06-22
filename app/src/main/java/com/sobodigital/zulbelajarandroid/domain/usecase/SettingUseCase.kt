package com.sobodigital.zulbelajarandroid.domain.usecase

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData

interface SettingUseCase {

    fun getThemeSettings(): LiveData<Any>

    suspend fun clearAllPreference()

    suspend fun <T> saveSetting(key: Preferences.Key<T>, value: T)

}

