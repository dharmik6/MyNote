package com.mynote.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.mynote.R
import com.mynote.databinding.ActivityOnboardingBinding
import com.project.app.base.BaseActivity
import com.project.app.utils.extension.launch
import com.project.app.utils.extension.viewBinding

class OnboardingActivity : BaseActivity(), View.OnClickListener {
    private val binding by viewBinding { ActivityOnboardingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        setListener()
    }

    private fun setListener() {
        binding.btnStart.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_start -> {
                launch<LoginActivity>()
            }
        }
    }
}
