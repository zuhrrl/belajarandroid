package com.sobodigital.zulbelajarandroid.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadStorySession(

    val error: Boolean? = null,

    val message: String? = null
) : Parcelable

