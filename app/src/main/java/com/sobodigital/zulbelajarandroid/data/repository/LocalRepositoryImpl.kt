package com.sobodigital.zulbelajarandroid.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.domain.repository.LocalRepository
import com.sobodigital.zulbelajarandroid.utils.reduceFileImage
import com.sobodigital.zulbelajarandroid.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File

class LocalRepositoryImpl(
    private val context: Context,
    private val prefSetting: SettingPreferences
) : LocalRepository{

    override suspend fun clearAllPreference() {
        Log.d(TAG, "Trying to clear preference")
        prefSetting.clearAllPreference()
    }

    override suspend fun reduceImageSize(uri: Uri?): Result<File?> {
        return withContext(Dispatchers.IO) {
            try {
                val file = uri?.let { uriToFile(it, context).reduceFileImage() }
                Result.Success(file)
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        }

    }

    override fun <T> getPreferenceSetting(key: Preferences.Key<T>): Flow<Any> {
        return prefSetting.getPreferenceSetting(key)
    }

    override suspend fun <T> saveSetting(key: Preferences.Key<T>, value: T) {
        return prefSetting.saveSetting(key, value)
    }

    companion object {
        private  val TAG = LocalRepositoryImpl::class.simpleName

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: LocalRepositoryImpl? = null
        fun getInstance(
            context: Context,
            prefSetting: SettingPreferences
        ): LocalRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: LocalRepositoryImpl(context, prefSetting)
            }.also { instance = it }
    }
}