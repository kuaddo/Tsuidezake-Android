package jp.kuaddo.tsuidezake.util

import android.widget.Toast
import androidx.annotation.StringRes

sealed class ToastMessage

class ToastMessageRes(
    @StringRes val resId: Int,
    vararg val params: Any,
    val duration: Int = Toast.LENGTH_LONG
) : ToastMessage()

class ToastMessageText(val text: String, val duration: Int = Toast.LENGTH_LONG) :
    ToastMessage()
