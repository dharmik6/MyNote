package com.mynote.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mynote.R
import com.mynote.databinding.ActivitySplashBinding
import com.project.app.base.BaseActivity
import com.project.app.utils.PrefUtils
import com.project.app.utils.extension.launch
import com.project.app.utils.extension.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    @Inject
    lateinit var prefUtils : PrefUtils
    private val binding by viewBinding { ActivitySplashBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)



        // hide the status bar
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // hide the status bar
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        Handler(Looper.getMainLooper()).postDelayed({

            if (prefUtils.isUserLoggedIn()) {
                launch<HomeActivity>()
            } else {
                launch<OnboardingActivity>()
            }
            finish()
        }, 3000)
    }


}