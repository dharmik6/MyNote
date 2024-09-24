package com.project.app.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mynote.databinding.LayoutCameraPermissionDialogBinding

class CameraPermissionDialog(context: Context) : Dialog(context) {
    lateinit var binding: LayoutCameraPermissionDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutCameraPermissionDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(0))
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        binding.btnOk.setOnClickListener {
            val settings = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            )
            context.startActivity(settings)
            dismiss()
        }
    }
}
