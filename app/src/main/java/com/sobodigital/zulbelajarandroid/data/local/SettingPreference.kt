package com.sobodigital.zulbelajarandroid.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun <T> getPreferenceSetting(key: Preferences.Key<T>): Flow<Any> {
        return dataStore.data.map { preferences ->
            preferences[key] ?: false
        }
    }

    suspend fun <T> saveSetting(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null
        val THEME_KEY = booleanPreferencesKey("theme_setting")
        val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token_key")

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}