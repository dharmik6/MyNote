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
import com.mynote.databinding.ActivityChangePasswordBinding
import com.mynote.viewmodel.FirebaseUserViewModel
import com.project.app.base.BaseActivity
import com.project.app.utils.ValidationUtils
import com.project.app.utils.extension.showToast
import com.project.app.utils.extension.viewBinding
import com.project.app.utils.resource.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : BaseActivity() , OnClickListener {
    private val binding by viewBinding { ActivityChangePasswordBinding.inflate(layoutInflater) }
    private val mViewModel : FirebaseUserViewModel by viewModels()
    private  val TAG = "ChangePasswordActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        observer()
        setListener()
    }

    private fun setListener() {
        binding.back.setOnClickListener(this)
        binding.btnSavePwd.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.back -> {
                onBackPressedDispatcher.onBackPressed()
            }
            binding.btnSavePwd -> {
                if (validation()){
                    changePassword()
                }
            }
        }
    }

    private fun validation() : Boolean{
        if (!ValidationUtils.isValidPassword(binding.etCtPwd.text.toString())){
            binding.layoutCtPwd.error = getString(R.string.enter_a_valid_password)
            return false
        }else{
            binding.layoutCtPwd.error = null
        }
        if (!ValidationUtils.isValidPassword(binding.etNewPwd.text.toString())){
            binding.layoutNewPwd.error = getString(R.string.enter_a_valid_password)
            return false
        }else{
            binding.layoutNewPwd.error = null
        }
        if (!ValidationUtils.isValidPassword(binding.etRetypePwd.text.toString())){
            binding.layoutRetypePwd.error = getString(R.string.enter_a_valid_password)
            return false
        }else{
            binding.layoutRetypePwd.error = null
        }
        if (!binding.etRetypePwd.text.toString().equals(binding.etNewPwd.text.toString())){
            binding.layoutRetypePwd.error = getString(R.string.password_are_not_match)
            return false
        }else{
            binding.layoutRetypePwd.error = null
        }

        return true
    }

    private fun changePassword(){
        val newPassword = binding.etNewPwd.text.toString().trim()
        val retypePassword = binding.etRetypePwd.text.toString().trim()
        val currentPassword = binding.etCtPwd.text.toString().trim()
        mViewModel.changePassword(currentPassword = currentPassword, newPassword = newPassword, retypePassword = retypePassword)
    }

    private fun observer(){
        mViewModel.changePasswordLiveData.observe(this){state ->
            when(state.status){
                Status.SUCCESS -> {
                    hideProgress()
                    showToast("Password has changed")
                    onBackPressedDispatcher.onBackPressed()
                }
                Status.ERROR -> {
                    hideProgress()
                    Log.e(TAG, "observer: error , ${state.error.toString()}", )
                    showToast(state.error?.errorMessage.toString())
                }
                Status.LOADING ->{
                    showProgress()
                }
            }
        }
    }
}