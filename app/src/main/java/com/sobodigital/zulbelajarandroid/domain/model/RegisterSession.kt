package com.sobodigital.zulbelajarandroid.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterSession(

    val error: Boolean? = null,

    val message: String? = null
) : Parcelable
