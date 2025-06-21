package com.sobodigital.zulbelajarandroid.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterData(
    val password: String? = null,

    val name: String? = null,

    val email: String? = null
) : Parcelable