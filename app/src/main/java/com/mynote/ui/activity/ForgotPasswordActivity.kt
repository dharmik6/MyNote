package com.mynote.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mynote.R
import com.mynote.databinding.ActivityForgotPasswordBinding
import com.mynote.viewmodel.FirebaseUserViewModel
import com.project.app.base.BaseActivity
import com.project.app.utils.ValidationUtils
import com.project.app.utils.extension.showToast
import com.project.app.utils.extension.viewBinding
import com.project.app.utils.resource.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity() , OnClickListener {
    private val binding by viewBinding { ActivityForgotPasswordBinding.inflate(layoutInflater) }
    private  val TAG = "ForgotPasswordActivity"
    private val mViewModel :FirebaseUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setListener()
    }

    private  fun setListener(){
        binding.back.setOnClickListener(this)
        binding.sendCode.setOnClickListener(this)

    }
    override fun onClick(v: View?) {
        when(v){
            binding.back -> {
                onBackPressedDispatcher.onBackPressed()
            }
            binding.sendCode -> {
                if (validation()){
                    sendRequest()
                }
            }
        }
    }

    private fun validation() : Boolean{
        if (!ValidationUtils.isValidEmail(binding.emailEditText.text.toString())){
            binding.emailTextLayout.error = "enter a valid email!"
            Log.d(TAG, "validation: return false")
            return false
        }else{
            binding.emailTextLayout.error = null
        }
        Log.d(TAG, "validation: return true")
        return true
    }

    private fun sendRequest(){
        val email = binding.emailEditText.text.toString().trim()
        mViewModel.resetPassword(email)
        mViewModel.resetPasswordLiveData.observe(this){ state ->
            when(state.status){
                Status.SUCCESS ->{
                    showToast("Request send on $email")
                    onBackPressedDispatcher.onBackPressed()
                    hideProgress()
                }
                Status.ERROR ->{
                    val error = state.error
                    Log.d(TAG, "signInUser: error = ${error?.errorMessage} , code = ${error?.errorCode}")
                    showToast(error?.errorMessage ?: "")
                    hideProgress()
                }
                Status.LOADING -> {
                    showProgress()
                }
            }
        }
    }

}