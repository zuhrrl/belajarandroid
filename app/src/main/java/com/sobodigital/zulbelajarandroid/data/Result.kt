package com.sobodigital.zulbelajarandroid.data

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String? = null, val code: Int? = null) : Result<Nothing>()
}