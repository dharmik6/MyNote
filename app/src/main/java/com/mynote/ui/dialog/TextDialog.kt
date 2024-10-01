package com.mynote.ui.dialog

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mynote.R
import com.mynote.app.MyApp
import com.mynote.databinding.TextDialogBinding
import com.project.app.utils.PrefUtils
import com.project.app.utils.extension.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TextDialog : BottomSheetDialogFragment(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {

    @Inject
    lateinit var mPrefUtils: PrefUtils
    private val binding by viewBinding { TextDialogBinding.inflate(layoutInflater) }
    val sizes = arrayOf("Small", "Medium", "Large")
    private val TAG = "TextDialog"
    var selectedSize = -1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setListener()

        binding.spinner.onItemSelectedListener = this

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        binding.spinner.setAdapter(adapter)
        binding.spinner.setSelection(selectedSize)
        return binding.root
    }

    private fun setListener() {
        binding.close.setOnClickListener(this)
    }


    fun adjustFontScale( configuration: Configuration, scale: Float) {
        configuration.fontScale = scale
        val metrics = resources.displayMetrics

        val wm =
            requireContext().getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)

        val updatedConfiguration = Configuration(configuration)
        updatedConfiguration.fontScale = scale

        val context = requireContext().applicationContext.createConfigurationContext(updatedConfiguration)

        requireContext().resources.updateConfiguration(updatedConfiguration, metrics)

        requireActivity().recreate()
    }


    override fun onClick(v: View?) {
        when (v) {
            binding.close -> {
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: selectedSize = $selectedSize")
        if (selectedSize != -1){
            mPrefUtils.saveTextSize(selectedSize)

            when (selectedSize) {
                0 -> {
                    adjustFontScale(resources.configuration, 0.8f)
                }

                1 -> {
                    adjustFontScale(resources.configuration, 1.0f)
                }

                2 -> {
                    adjustFontScale(resources.configuration, 1.2f)
                }
            }
        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(TAG, "onItemSelected: selected text size = ${sizes[position]}")
        selectedSize = position
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}