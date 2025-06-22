package com.sobodigital.zulbelajarandroid.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
class UploadStoryData(

    val file: File? = null,

    val description: String? = null
) : Parcelable
