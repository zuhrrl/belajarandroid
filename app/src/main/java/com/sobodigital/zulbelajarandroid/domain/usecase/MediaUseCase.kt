package com.sobodigital.zulbelajarandroid.domain.usecase

import android.net.Uri
import com.sobodigital.zulbelajarandroid.data.Result
import java.io.File

interface MediaUseCase {

    suspend fun reduceImageSize(uri: Uri?): Result<File?>
}