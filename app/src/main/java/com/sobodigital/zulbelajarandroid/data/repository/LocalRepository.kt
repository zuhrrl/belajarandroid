package com.sobodigital.zulbelajarandroid.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.utils.reduceFileImage
import com.sobodigital.zulbelajarandroid.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LocalRepository(private val context: Context) {
    suspend fun reduceImageSize(uri: Uri?): Result<File?> {
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

    companion object {
        private  val TAG = LocalRepository::class.simpleName

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: LocalRepository? = null
        fun getInstance(
            context: Context
        ): LocalRepository =
            instance ?: synchronized(this) {
                instance ?: LocalRepository(context)
            }.also { instance = it }
    }
}