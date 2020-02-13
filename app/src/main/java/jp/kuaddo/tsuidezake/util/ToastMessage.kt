package jp.kuaddo.tsuidezake.util

import android.widget.Toast
import androidx.annotation.StringRes

sealed class ToastMessage

data class ToastMessageRes(@StringRes val resId: Int, val duration: Int = Toast.LENGTH_LONG) :
    ToastMessage()

data class ToastMessageResParams(
    @StringRes val resId: Int,
    val params: List<String>,
    val duration: Int = Toast.LENGTH_LONG
) : ToastMessage()

data class ToastMessageText(val text: String, val duration: Int = Toast.LENGTH_LONG) :
    ToastMessage()
