package com.sobodigital.zulbelajarandroid.domain.usecase

import android.net.Uri
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.repository.LocalRepository
import java.io.File

class MediaInteractor(private val localRepository: LocalRepository) : MediaUseCase {
    override suspend fun reduceImageSize(uri: Uri?): Result<File?> {
        return localRepository.reduceImageSize(uri)
    }
}