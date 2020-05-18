package jp.kuaddo.tsuidezake.util

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

sealed class SnackbarMessage

class SnackbarMessageRes(
    @StringRes val resId: Int,
    vararg val params: Any,
    val duration: Int = Snackbar.LENGTH_LONG
) : SnackbarMessage()

class SnackbarMessageText(val text: String, val duration: Int = Snackbar.LENGTH_LONG) :
    SnackbarMessage()
