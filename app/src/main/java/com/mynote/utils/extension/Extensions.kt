package com.project.app.utils.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.io.Serializable
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


///////////////////////////////////////////////////////////////////////////
// LUCIFER //
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// ViewBinding
///////////////////////////////////////////////////////////////////////////

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T,
    ) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

inline fun <T : ViewBinding> Fragment.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T,
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

///////////////////////////////////////////////////////////////////////////
// Colors & Dimensions
///////////////////////////////////////////////////////////////////////////

fun View.bindColor(@ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(context, id)
}

fun View.bindDimen(@DimenRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context.resources.getDimension(id)
}

fun View.bindString(@StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context.getString(id)
}

fun Activity.bindColor(@ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(this, id)
}

fun Activity.bindDimen(@DimenRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    resources.getDimension(id)
}

fun Activity.bindString(@StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    getString(id)
}

fun Fragment.bindColor(@ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(context!!, id)
}

fun Fragment.bindDimen(@DimenRes id: Int): Lazy<Float> = lazy(LazyThreadSafetyMode.NONE) {
    context!!.resources.getDimension(id)
}

fun Fragment.bindString(@StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context!!.getString(id)
}

fun Any.bindColor(context: Context, @ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(context, id)
}

fun Any.bindDimen(context: Context, @DimenRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context.resources.getDimension(id)
}

fun Any.bindString(context: Context, @StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context.getString(id)
}

fun Any.bindColor(view: View, @ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(view.context, id)
}

fun Any.bindDimen(view: View, @DimenRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    view.context.resources.getDimension(id)
}

fun Any.bindString(view: View, @StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    view.context.getString(id)
}

fun getColor(context: Context, @ColorRes id: Int): Int {
    return ContextCompat.getColor(context, id)
}

fun getColorStateList(context: Context, @ColorRes id: Int): ColorStateList {
    return ColorStateList.valueOf(ContextCompat.getColor(context, id))
}

fun getColorFromAttr(context: Context, @ColorInt id: Int): Int {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(id, typedValue, true)
    return ContextCompat.getColor(context, typedValue.resourceId)
}

fun getDrawable(context: Context, @DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(context, id)
}

///////////////////////////////////////////////////////////////////////////
// Logger
///////////////////////////////////////////////////////////////////////////

fun luciferLog(message: String?) {
//    if (BuildConfig.DEBUG) {
    Log.e("lucifer", message ?: "null")
//    }
}

fun luciferLog(tag: String, message: String?) {
//    if (BuildConfig.DEBUG) {
    Log.e(tag, message ?: "null")
//    }
}

fun luciferLog(message: Any?) {
//    if (BuildConfig.DEBUG) {
    Log.e("lucifer", Gson().toJson(message ?: "null"))
//    }
}

fun luciferLog(tag: String, message: Any?) {
//    if (BuildConfig.DEBUG) {
    Log.e("lucifer", Gson().toJson(message ?: "null"))
//    }
}

fun luciferLogLong(str: String) {
    if (str.length > 4000) {
        Log.e("lucifer", str.substring(0, 4000))
        luciferLogLong(str.substring(4000))
    } else Log.e("lucifer", str)
}

///////////////////////////////////////////////////////////////////////////
// Intents
///////////////////////////////////////////////////////////////////////////

inline fun <reified T : Activity> Context.launch(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(block))
}

inline fun <reified T : Activity> Fragment.launch(block: Intent.() -> Unit = {}) {
    startActivity(Intent(requireActivity(), T::class.java).apply(block))
}

///////////////////////////////////////////////////////////////////////////
// ImageLoading
///////////////////////////////////////////////////////////////////////////

fun ImageView.loadWithGlide(uri: Uri) {
    Glide.with(this.context).load(uri).into(this)
}

fun ImageView.loadWithGlide(string: String) {
    Glide.with(this.context).load(string).into(this)
}

fun ImageView.loadWithGlide(drawable: Drawable) {
    Glide.with(this.context).load(drawable).into(this)
}

fun ImageView.loadWithGlide(bitmap: Bitmap) {
    Glide.with(this.context).load(bitmap).into(this)
}

fun ImageView.loadWithGlide(@RawRes @DrawableRes resourceId: Int) {
    Glide.with(this.context).load(resourceId).into(this)
}

fun ImageView.loadWithGlide(url: URL) {
    Glide.with(this.context).load(url).into(this)
}

///////////////////////////////////////////////////////////////////////////
// Toaster
///////////////////////////////////////////////////////////////////////////

fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

///////////////////////////////////////////////////////////////////////////
// Navigation
///////////////////////////////////////////////////////////////////////////

fun finishFragment(@IdRes destinationId: Int): NavOptions {
    return NavOptions.Builder().setPopUpTo(destinationId, true).build()
}

///////////////////////////////////////////////////////////////////////////
// Refresh
///////////////////////////////////////////////////////////////////////////

fun SwipeRefreshLayout.refreshAndDo(task: () -> Unit) {
    if (isRefreshing) isRefreshing = false
    setOnRefreshListener {
        task()
    }
}

///////////////////////////////////////////////////////////////////////////
// Animation
///////////////////////////////////////////////////////////////////////////

fun TextView.setTextWithAnimation(
    text: String, duration: Long = 300, completion: (() -> Unit)? = null
) {
    fadOutAnimation(duration) {
        this.text = text
        fadInAnimation(duration) {
            completion?.let {
                it()
            }
        }
    }
}

fun View.fadOutAnimation(
    duration: Long = 100, visibility: Int = View.INVISIBLE, completion: (() -> Unit)? = null
) {
    animate().alpha(0.5f).setDuration(duration).withEndAction {
        this.visibility = visibility
        completion?.let {
            it()
        }
    }
}

fun View.fadInAnimation(duration: Long = 300, completion: (() -> Unit)? = null) {
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1f).setDuration(duration).withEndAction {
        completion?.let {
            it()
        }
    }
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
        key, T::class.java
    )
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

inline fun <reified T : Serializable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Serializable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

fun View.translateUpAnimation(
    duration: Long = 300, visibility: Int = View.VISIBLE, completion: (() -> Unit)? = null
) {
    animate().translationYBy(1f).duration = duration
}

fun View.setRotateAnimation(
    duration: Long = 5000,
    repeatCount: Int = Animation.INFINITE,
) {
    val rotate = RotateAnimation(
        0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    rotate.repeatCount = repeatCount
    rotate.duration = duration
    rotate.interpolator = LinearInterpolator()
    startAnimation(rotate)
}

fun View.setScaleUpAnimation(
    scaleUpDuration: Long = 3000,
    scaleDownDuration: Long = 2000,
) {
    animate().scaleX(1.1f).scaleY(1.1f).setDuration(scaleUpDuration).withEndAction {
        animate().scaleX(1f).scaleY(1f).setDuration(scaleDownDuration).withEndAction {
            setScaleUpAnimation(scaleUpDuration, scaleDownDuration)
        }
    }
}

fun Context.getColorFromResource(id: Int): Int {
    return ContextCompat.getColor(this, id)
}


///////////////////////////////////////////////////////////////////////////
// for change the over all app text size
///////////////////////////////////////////////////////////////////////////

fun adjustFontScale(context: Activity,configuration: Configuration, scale: Float) {
    configuration.fontScale = scale
    val metrics = context.resources.displayMetrics

    val wm =
        context.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
    wm.defaultDisplay.getMetrics(metrics)

    val updatedConfiguration = Configuration(configuration)
    updatedConfiguration.fontScale = scale

    val mContext =
        context.applicationContext.createConfigurationContext(updatedConfiguration)

    context.resources.updateConfiguration(updatedConfiguration, metrics)

    context.recreate()
}



///////////////////////////////////////////////////////////////////////////
// getCurrentTime
///////////////////////////////////////////////////////////////////////////

fun urlToBitmap(urlString: String): Bitmap {
    val url = URL(urlString)
    return BitmapFactory.decodeStream(url.openConnection().getInputStream())
}


///////////////////////////////////////////////////////////////////////////
// getCurrentTime
///////////////////////////////////////////////////////////////////////////

fun AppCompatActivity.showSoftKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = getSystemService(InputMethodManager::class.java)
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Fragment.showSoftKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = requireContext().getSystemService(InputMethodManager::class.java)
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Context.showSoftKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = getSystemService(InputMethodManager::class.java)
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

///////////////////////////////////////////////////////////////////////////
// getCurrentTime
///////////////////////////////////////////////////////////////////////////

fun getCurrentTimeZone(): String {
    val timeZone: TimeZone = TimeZone.getDefault()
    val timeZoneName = timeZone.displayName
    val timeZoneID = timeZone.id
    val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(
        Date()
    )

    return currentTime
}
