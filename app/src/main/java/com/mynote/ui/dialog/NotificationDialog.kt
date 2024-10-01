package com.mynote.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mynote.databinding.DialogNotificationBinding
import com.project.app.utils.PrefUtils
import com.project.app.utils.extension.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationDialog : BottomSheetDialogFragment(),OnClickListener {

    @Inject
    lateinit var prefUtils: PrefUtils
    private val binding by viewBinding { DialogNotificationBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setListener()
        val state = prefUtils.getPushNotification()

        binding.mySwitch.isChecked =  state

        binding.mySwitch.setOnCheckedChangeListener{_, isChecked ->
            prefUtils.savePushNotification(isChecked)
        }
        return binding.root
    }

    private fun setListener() {
        binding.close.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when(v){
            binding.close -> {
                dismiss()
            }
        }
    }
}