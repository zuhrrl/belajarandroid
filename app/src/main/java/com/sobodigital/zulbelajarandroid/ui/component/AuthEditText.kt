package com.sobodigital.zulbelajarandroid.ui.component

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText


class AuthEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if(text != null && text.toString().trim().length < 8) {
            setError("Password tidak boleh kurang dari 8 karakter!", null)
            return
        }
        error = null
    }
}