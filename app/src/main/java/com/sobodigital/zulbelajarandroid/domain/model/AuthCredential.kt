package com.sobodigital.zulbelajarandroid.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class AuthCredential(
    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("password")
    val password: String? = null

) : Parcelable