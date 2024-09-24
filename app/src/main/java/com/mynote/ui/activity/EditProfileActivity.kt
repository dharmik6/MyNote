package com.mynote.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.net.toUri
import com.mynote.data.param.UserDataParam
import com.mynote.databinding.ActivityEditProfileBinding
import com.mynote.utils.ImagePickerBottomSheetDialog
import com.mynote.viewmodel.FirebaseUserViewModel
import com.project.app.base.BaseActivity
import com.project.app.utils.AppConstant
import com.project.app.utils.PrefUtils
import com.project.app.utils.extension.loadWithGlide
import com.project.app.utils.extension.showToast
import com.project.app.utils.extension.viewBinding
import com.project.app.utils.resource.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileActivity : BaseActivity(), OnClickListener,
    ImagePickerBottomSheetDialog.OnViewClickedListener {

    @Inject
    lateinit var prefUtils: PrefUtils
    private val binding by viewBinding { ActivityEditProfileBinding.inflate(layoutInflater) }
    val dialog = ImagePickerBottomSheetDialog()
    private  val mViewModel : FirebaseUserViewModel by viewModels()
    private val TAG = "EditProfileActivity"
    var imageUrl : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setListener()
        getData()
        observer()
    }

    private fun setListener() {
        binding.back.setOnClickListener(this)
        binding.changeImage.setOnClickListener(this)
        binding.btnSaveCng.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v) {
            binding.back -> {
                onBackPressedDispatcher.onBackPressed()
            }

            binding.changeImage -> {
                startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),AppConstant.GALLERY_PERMISSION_REQUEST_CODE)
            }

            binding.btnSaveCng -> {
                updateData()
            }
        }
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

        mViewModel.uploadImageLiveData.observe(this){ state ->
            when(state.status){
                Status.SUCCESS -> {
                    hideProgress()
                    if (state.data != null)
                        imageUrl = state.data
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

        mViewModel.updateUserLiveData.observe(this){ state ->
            when(state.status){
                Status.SUCCESS -> {
                    hideProgress()
                    showToast("Data updated")
                    onBackPressedDispatcher.onBackPressed()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.GALLERY_PERMISSION_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null)
        {
            val image = data.data!!
            binding.profileImg.loadWithGlide(image)
            Log.d(TAG, "onImageSelected: image = $image")
            Log.d(TAG, "onImageSelected: image uri = ${image}")
            mViewModel.uploadImage(image)
        }
    }

    private fun setData(data : UserDataParam) {
      binding.nameEditText.setText(data.name)
      binding.emailEditText.setText(data.email)
        if (data.profileImage != null){
            binding.profileImg.loadWithGlide(data.profileImage)
        }
    }

    private fun updateData(){
        val userId = prefUtils.getAuthToken()
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val userData = UserDataParam(name,email, profileImage = imageUrl)
        if (userId != null)
            mViewModel.updateUser(userId,userData)
    }


    override fun onImageSelected(image: String) {

        dialog.dismiss()
    }

    override fun onSelectAvatar() {
        TODO("Not yet implemented")
    }
}