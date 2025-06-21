package com.sobodigital.zulbelajarandroid.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(

    val id: String? = null,

    val photoUrl: String? = null,

    val name: String? = null,

    val description: String? = null,

    val lon: Double? = null,

    val lat: Double? = null
) : Parcelable