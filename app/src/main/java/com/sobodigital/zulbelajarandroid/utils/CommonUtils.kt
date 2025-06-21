package com.sobodigital.zulbelajarandroid.utils

import android.content.Context
import android.content.Intent
import com.sobodigital.zulbelajarandroid.presentation.pages.LoginActivity
import com.sobodigital.zulbelajarandroid.presentation.pages.MainActivity

fun navigateHome(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

fun navigateToLogin(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}