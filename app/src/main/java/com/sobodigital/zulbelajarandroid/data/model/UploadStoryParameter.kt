package com.sobodigital.zulbelajarandroid.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
class UploadStoryParameter(

    @field:SerializedName("file")
    val file: File? = null,

    @field:SerializedName("description")
    val description: String? = null
) : Parcelable