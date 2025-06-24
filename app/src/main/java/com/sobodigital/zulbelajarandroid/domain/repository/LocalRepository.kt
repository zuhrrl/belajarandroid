package com.sobodigital.zulbelajarandroid.domain.repository

import android.net.Uri
import androidx.datastore.preferences.core.Preferences
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.model.Story
import kotlinx.coroutines.flow.Flow
import java.io.File

interface LocalRepository {

    suspend fun clearAllPreference()

    suspend fun reduceImageSize(uri: Uri?): Result<File?>

    fun <T> getPreferenceSetting(key: Preferences.Key<T>): Flow<Any>

    suspend fun <T> saveSetting(key: Preferences.Key<T>, value: T)

}