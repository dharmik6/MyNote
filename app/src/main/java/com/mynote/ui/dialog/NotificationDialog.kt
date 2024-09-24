package com.mynote.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mynote.databinding.DialogNotificationBinding
import com.project.app.utils.extension.viewBinding

class NotificationDialog : BottomSheetDialogFragment(),OnClickListener {
    private val binding by viewBinding { DialogNotificationBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setListener()
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