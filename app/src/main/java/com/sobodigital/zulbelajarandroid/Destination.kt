package com.sobodigital.zulbelajarandroid

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination(val name: String, val image: Int, val description: String) : Parcelable
