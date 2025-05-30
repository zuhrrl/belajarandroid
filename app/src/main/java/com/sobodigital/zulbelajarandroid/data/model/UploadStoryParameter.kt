package com.sobodigital.zulbelajarandroid.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
class UploadStoryParameter(
    val file: File? = null,
    val description: String? = null
) : Parcelable