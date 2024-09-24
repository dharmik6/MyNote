package com.project.app.base


import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mynote.ui.dialog.LoadingDialog
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
open class BaseFragment : Fragment() {

    private var dialog: LoadingDialog? = null
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var nextUrl: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    fun showProgress() {
        if (dialog == null) {
            initDialog()
        } else {
            if (dialog != null && dialog?.isShowing == true) {
                dialog?.dismiss()
                initDialog()
            } else {
                initDialog()
            }
        }
    }

    private fun initDialog() {
        if (context != null && isVisible) {
            dialog = LoadingDialog(requireContext())
            dialog?.show()
        }
    }

    fun hideProgress() {
        if (context != null && isVisible && dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

}