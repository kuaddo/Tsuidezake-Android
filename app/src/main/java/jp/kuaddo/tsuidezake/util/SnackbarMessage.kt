package jp.kuaddo.tsuidezake.util

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

sealed class SnackbarMessage

data class SnackbarMessageRes(@StringRes val resId: Int, val duration: Int = Snackbar.LENGTH_LONG) :
    SnackbarMessage()

data class SnackbarMessageResParams(
    @StringRes val resId: Int,
    val params: List<String>,
    val duration: Int = Snackbar.LENGTH_LONG
) : SnackbarMessage()

data class SnackbarMessageText(val text: String, val duration: Int = Snackbar.LENGTH_LONG) :
    SnackbarMessage()
