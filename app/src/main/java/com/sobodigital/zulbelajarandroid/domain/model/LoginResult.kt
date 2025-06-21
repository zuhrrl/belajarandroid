package com.sobodigital.zulbelajarandroid.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResult(

    val name: String? = null,

    val userId: String? = null,

    val token: String? = null
) : Parcelable