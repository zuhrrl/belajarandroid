package com.sobodigital.zulbelajarandroid.utils

import android.content.Context
import android.content.Intent
import com.sobodigital.zulbelajarandroid.ui.pages.MainActivity

fun navigateHome(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
   context.startActivity(intent)
}