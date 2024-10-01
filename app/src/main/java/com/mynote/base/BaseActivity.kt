package com.project.app.base

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mynote.R
import com.mynote.ui.activity.HomeActivity
import com.mynote.ui.dialog.LoadingDialog
import com.project.app.utils.extension.launch
import com.project.app.utils.extension.showToast
import com.project.app.utils.resource.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


abstract class BaseActivity : AppCompatActivity() {


    private var dialog: LoadingDialog? = null

    fun showProgress() {
        if (dialog == null) {
            initDialog()
        } else {
            if (dialog?.isShowing == true) {
                dialog?.dismiss()
                initDialog()
            } else {
                initDialog()
            }
        }
    }

    private fun initDialog() {
        if (!isFinishing) {
            dialog = LoadingDialog(this)
            dialog?.show()
        }
    }

    fun hideProgress() {
        if (!isFinishing && dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

    fun adjustFontScale(configuration: Configuration, scale: Float) {
        configuration.fontScale = scale
        val metrics = resources.displayMetrics

        val wm =
            this.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)

        val updatedConfiguration = Configuration(configuration)
        updatedConfiguration.fontScale = scale

        val context = this.applicationContext.createConfigurationContext(updatedConfiguration)

        this.resources.updateConfiguration(updatedConfiguration, metrics)

        this.recreate()
    }


}
