package com.project.app.utils.extension

import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat


fun TextView.setTopDrawable(@DrawableRes id: Int?) {
    val drawable = if (id != null && id != 0) {
        ContextCompat.getDrawable(context, id)
    } else null

    setCompoundDrawablesWithIntrinsicBounds(
        compoundDrawables[0],
        drawable,
        compoundDrawables[2],
        compoundDrawables[3]
    )
}


fun TextView.setEndDrawable(@DrawableRes id: Int?) {
    val drawable = if (id != null && id != 0) {
        ContextCompat.getDrawable(context, id)
    } else null

    setCompoundDrawablesWithIntrinsicBounds(
        compoundDrawables[0],
        compoundDrawables[1],
        drawable,
        compoundDrawables[3]
    )
}


fun TextView.setStartDrawable(@DrawableRes id: Int?) {
    val drawable = if (id != null && id != 0) {
        ContextCompat.getDrawable(context, id)
    } else null

    setCompoundDrawablesWithIntrinsicBounds(
        drawable,
        compoundDrawables[1],
        compoundDrawables[2],
        compoundDrawables[3]
    )
}


fun TextView.setBottomDrawable(@DrawableRes id: Int?) {
    val drawable = if (id != null && id != 0) {
        ContextCompat.getDrawable(context, id)
    } else null

    setCompoundDrawablesWithIntrinsicBounds(
        compoundDrawables[0],
        compoundDrawables[1],
        compoundDrawables[2],
        drawable
    )
}


fun TextView.setTextColorRes(@ColorRes color: Int) {
    setTextColor(context.getColorFromResource(color))
}

fun TextView.setTextFormatted(@StringRes id: Int, vararg formatArgs: String) {
    text = String.format(context.getString(id), *formatArgs)
}

fun EditText.isEmpty(): Boolean {
    return stringText().isEmpty()
}

fun EditText.stringText(trim: Boolean = true) =
    if (trim) text.toString().trim() else text.toString()


fun EditText.clear() {
    text?.clear()
}
