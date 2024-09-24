package com.mynote.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mynote.data.param.UserDataParam
import com.mynote.databinding.ActivitySettingsBinding
import com.mynote.databinding.AlertDialogBinding
import com.mynote.ui.dialog.NotificationDialog
import com.mynote.ui.dialog.TextDialog
import com.mynote.viewmodel.FirebaseUserViewModel
import com.project.app.base.BaseActivity
import com.project.app.utils.PrefUtils
import com.project.app.utils.extension.launch
import com.project.app.utils.extension.loadWithGlide
import com.project.app.utils.extension.showToast
import com.project.app.utils.extension.viewBinding
import com.project.app.utils.resource.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : BaseActivity(), OnClickListener {

    @Inject
    lateinit var prefUtils: PrefUtils
    private val binding by viewBinding { ActivitySettingsBinding.inflate(layoutInflater) }
    private  val mViewModel : FirebaseUserViewModel by viewModels()
    private val TAG = "SettingsActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        getData()
        observer()
        setListener()
    }

    private fun setListener() {
        binding.logout.setOnClickListener(this)
        binding.changePwd.setOnClickListener(this)
        binding.notification.setOnClickListener(this)
        binding.text.setOnClickListener(this)
        binding.back.setOnClickListener(this)
        binding.editProfile.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.logout -> {
                logOut()
            }

            binding.changePwd -> {
                launch<ChangePasswordActivity>()
            }

            binding.back -> {
                onBackPressedDispatcher.onBackPressed()
            }

            binding.editProfile -> {
                launch<EditProfileActivity>()
            }

            binding.notification -> {
                val dialog = NotificationDialog()
                dialog.show(supportFragmentManager, dialog.tag)
            }

            binding.text -> {
                val dialog = TextDialog()
                dialog.show(supportFragmentManager, dialog.tag)
            }
        }
    }

    private fun logOut() {
        val dialogBinding = AlertDialogBinding.inflate(LayoutInflater.from(this))

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.yesBtn.setOnClickListener {
            Firebase.auth.signOut()
            prefUtils.clearPref()
            launch<LoginActivity>()
            finish()
        }
        dialogBinding.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun getData(){
        val userId = prefUtils.getAuthToken()
        if (userId != null)
            mViewModel.getUserData(userId)
    }

    private fun observer(){
        mViewModel.getUserDataLiveData.observe(this){ state ->
            when(state.status){
                Status.SUCCESS -> {
                    hideProgress()
                    if (state.data != null)
                        setData(state.data)
                }
                Status.ERROR -> {
                    hideProgress()
                    Log.e(TAG, "observer: ${state.error}" )
                    showToast(state.error?.errorMessage.toString())
                }
                Status.LOADING -> {
                    showProgress()
                }
            }

        }
    }

    private fun setData(data : UserDataParam) {
        binding.userName.text = data.name
        binding.userEmail.text = data.email
        if (data.profileImage != null){
            binding.userImage.loadWithGlide(data.profileImage)
        }
    }

}