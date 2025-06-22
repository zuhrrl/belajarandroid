package com.sobodigital.zulbelajarandroid.domain.usecase

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.domain.repository.LocalRepository

class SettingInteractor(private val localRepository: LocalRepository) : SettingUseCase {

    override fun getThemeSettings(): LiveData<Any> {
        return localRepository.getPreferenceSetting(SettingPreferences.THEME_KEY).asLiveData()
    }

    override suspend fun clearAllPreference() {
        return localRepository.clearAllPreference()
    }

    override suspend fun <T> saveSetting(key: Preferences.Key<T>, value: T) {
        return localRepository.saveSetting(key, value)
    }
}