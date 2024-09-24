package com.mynote.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mynote.R
import com.mynote.databinding.DialogColorBinding
import com.mynote.interfaces.OnColorSelectListener
import com.project.app.utils.extension.getColor
import com.project.app.utils.extension.viewBinding

class ColorDialog : DialogFragment() , OnClickListener {
    val binding by viewBinding { DialogColorBinding.inflate(layoutInflater) }
    private var onColorSelectListener: OnColorSelectListener? = null
    private val TAG = "ColorDialog"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getDialog()!!.getWindow()!!.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        setListeners()
        return  binding.root
    }

    private fun setListeners(){
        binding.color1.setOnClickListener(this)
        binding.color2.setOnClickListener(this)
        binding.color3.setOnClickListener(this)
        binding.color4.setOnClickListener(this)
        binding.color5.setOnClickListener(this)
        binding.color6.setOnClickListener(this)
        binding.color7.setOnClickListener(this)
        binding.color8.setOnClickListener(this)
        binding.color9.setOnClickListener(this)
        binding.color10.setOnClickListener(this)
        binding.color11.setOnClickListener(this)
        binding.color12.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.color1 -> {
                val color = binding.color1.imageTintList!!.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and color)
                onColorSelectListener!!.onColorSelectListener(hexColor)
                Log.d(TAG, "onClick: hexColor = $hexColor")

            }
            binding.color2 -> {
                val color = binding.color2.imageTintList!!.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and color)
                onColorSelectListener!!.onColorSelectListener(hexColor)
                Log.d(TAG, "onClick: hexColor = $hexColor")

            }
            binding.color3 -> {
                val color = binding.color3.imageTintList!!.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and color)
                onColorSelectListener!!.onColorSelectListener(hexColor)
                Log.d(TAG, "onClick: hexColor = $hexColor")

            }
            binding.color4 -> {
                val color = binding.color4.imageTintList!!.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and color)
                onColorSelectListener!!.onColorSelectListener(hexColor)
                Log.d(TAG, "onClick: hexColor = $hexColor")

            }
            binding.color5 -> {
                val color = binding.color5.imageTintList!!.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and color)
                onColorSelectListener!!.onColorSelectListener(hexColor)
                Log.d(TAG, "onClick: hexColor = $hexColor")

            }
            binding.color6 -> {
                val color = binding.color6.imageTintList!!.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and color)
                onColorSelectListener!!.onColorSelectListener(hexColor)
                Log.d(TAG, "onClick: hexColor = $hexColor")

            }
            binding.color7 -> {
                val color = binding.color7.imageTintList!!.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and color)
                onColorSelectListener!!.onColorSelectListener(hexColor)
                Log.d(TAG, "onClick: hexColor = $hexColor")

            }
            binding.color8 -> {
                val color = binding.color8.imageTintList!!.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and color)
                onColorSelectListener!!.onColorSelectListener(hexColor)
                Log.d(TAG, "onClick: hexColor = $hexColor")

            }
            binding.color9 -> {
                val color = binding.color9.imageTintList!!.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and color)
                onColorSelectListener!!.onColorSelectListener(hexColor)
                Log.d(TAG, "onClick: hexColor = $hexColor")

            }
            binding.color10 -> {
                val color = binding.color10.imageTintList!!.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and color)
                onColorSelectListener!!.onColorSelectListener(hexColor)
                Log.d(TAG, "onClick: hexColor = $hexColor")

            }
            binding.color11 -> {
                val color = binding.color11.imageTintList!!.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and color)
                onColorSelectListener!!.onColorSelectListener(hexColor)
                Log.d(TAG, "onClick: hexColor = $hexColor")

            }
            binding.color12 -> {
                val color = binding.color12.imageTintList!!.defaultColor
                val hexColor = String.format("#%06X", 0xFFFFFF and color)
                onColorSelectListener!!.onColorSelectListener(hexColor)
                Log.d(TAG, "onClick: hexColor = $hexColor")

            }
        }

    }

    fun setOnColorSelectListener(onColorSelectListener: OnColorSelectListener?) {
        this.onColorSelectListener = onColorSelectListener
    }

    fun dialogDismiss(){
        dismiss()
    }

}