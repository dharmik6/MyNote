package com.mynote.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.mynote.R
import com.mynote.data.param.UserDataParam
import com.mynote.databinding.ActivityRegisterBinding
import com.mynote.viewmodel.FirebaseUserViewModel
import com.project.app.base.BaseActivity
import com.project.app.utils.AppConstant
import com.project.app.utils.PrefUtils
import com.project.app.utils.ValidationUtils
import com.project.app.utils.extension.launch
import com.project.app.utils.extension.luciferLog
import com.project.app.utils.extension.showToast
import com.project.app.utils.extension.viewBinding
import com.project.app.utils.resource.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : BaseActivity(), OnClickListener {

    @Inject
    lateinit var prefUtils: PrefUtils
    private val binding by viewBinding { ActivityRegisterBinding.inflate(layoutInflater) }
    private val TAG = "RegisterActivity"

    private val mViewModel : FirebaseUserViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        googleSignIn()
        setListener()
        observer()
    }

    private fun setListener() {
        binding.btnRegister.setOnClickListener(this)
        binding.txtLogin.setOnClickListener(this)
        binding.btnGoogle.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnRegister -> {
                if (validation())
                    createUser()
            }

            binding.txtLogin -> {
                launch<LoginActivity>()
            }
            binding.btnGoogle -> {
                // Sign out or revoke access to ensure the login screen is shown again
                googleSignInClient.signOut().addOnCompleteListener(this) {
                    val signInIntent = googleSignInClient.signInIntent
                    startActivityForResult(signInIntent, AppConstant.GOOGLE_REQUEST_CODE)
                }
            }
        }
    }

    fun createUser(){
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.pwdEditText.text.toString().trim()
        mViewModel.createUser(email,password)
        mViewModel.createUserLiveData.observe(this){ state ->
            when(state.status){
                Status.SUCCESS -> {
                    if (state.data != null){
                        prefUtils.saveAuthToken(state.data.uid)
                        insertUserData()
                    }else{
                        showToast(getString(R.string.something_went_wrong))
                    }
                }
                Status.ERROR -> {
                    val error = state.error
                    luciferLog(error?.errorMessage)
                    showToast(error?.errorMessage ?: "")
                    hideProgress()
                }
                Status.LOADING -> {
                    showProgress()
                }
            }

        }
    }

     fun createUser(data: FirebaseUser) {

        val userId = prefUtils.getAuthToken()
        val userData = UserDataParam(name = data.displayName, email = data.email)
        Log.d(TAG, "insertUserData: userData = $userData")
        if (userId != null)
            mViewModel.insertOrUpdateUser(userId,userData)

        mViewModel.insertOrUpdateUserLiveData.observe(this){ state ->
            when(state.status){
                Status.SUCCESS -> {
                    if (state.data == true){
                        launch<HomeActivity>()
                        finish()
                    }else{
                        showToast(getString(R.string.something_went_wrong))
                    }
                    hideProgress()
                }
                Status.ERROR -> {
                    val error = state.error
                    luciferLog(error?.errorMessage)
                    showToast(error?.errorMessage ?: "")
                    hideProgress()
                }
                Status.LOADING -> {
                    showProgress()
                }
            }
        }
    }

    private  fun insertUserData(){
        val userId = prefUtils.getAuthToken()
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.pwdEditText.text.toString().trim()
        val userData = UserDataParam(name = name, email = email, password = password)
        Log.d(TAG, "insertUserData: userData = $userData")
        if (userId != null)
            mViewModel.insertOrUpdateUser(userId,userData)

        mViewModel.insertOrUpdateUserLiveData.observe(this){ state ->
            when(state.status){
                Status.SUCCESS -> {
                    if (state.data == true){
                        launch<HomeActivity>()
                        finish()
                    }else{
                        showToast(getString(R.string.something_went_wrong))
                    }
                    hideProgress()
                }
                Status.ERROR -> {
                    val error = state.error
                    luciferLog(error?.errorMessage)
                    showToast(error?.errorMessage ?: "")
                    hideProgress()
                }
                Status.LOADING -> {
                    showProgress()
                }
            }
        }
    }

    private fun validation(): Boolean {
        if (ValidationUtils.isEmptyFiled(binding.nameEditText.text.toString())) {
            binding.nameTextLayout.error = getString(R.string.enter_a_your_name)
            return false
        } else {
            binding.emailTextLayout.error = null
        }
        if (!ValidationUtils.isValidEmail(binding.emailEditText.text.toString())) {
            binding.emailTextLayout.error = getString(R.string.enter_a_valid_email)
            return false
        } else {
            binding.emailTextLayout.error = null
        }
        if (!ValidationUtils.isValidPassword(binding.pwdEditText.text.toString())) {
            binding.pwdTextLayout.error = getString(R.string.enter_a_valid_password)
            return false
        } else {
            binding.pwdTextLayout.error = null
        }
        if (!binding.rePwdEditText.text.toString().equals(binding.pwdEditText.text.toString())) {
            binding.rePwdTextLayout.error = getString(R.string.password_are_not_match)
            return false
        } else {
            binding.rePwdTextLayout.error = null
        }

        return true
    }

     fun observer(){
        mViewModel.googleSignInLiveData.observe(this){ state ->
            when(state.status){
                Status.SUCCESS -> {
                    if (state.data != null){
                        prefUtils.saveAuthToken(state.data.uid)
                        createUser(state.data)
                    }else{
                        showToast(getString(R.string.something_went_wrong))
                    }
                    hideProgress()
                }
                Status.ERROR -> {
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

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == AppConstant.GOOGLE_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "onActivityResult: ${account.idToken}")
                if (account.idToken != null){
                    mViewModel.googleSignIn(account.idToken!!)
                }

                Log.d(TAG, "account name = ${account.displayName}, email = ${account.email},  ")
            } catch (e: ApiException) {
                Log.e(TAG, "Google sign in failed", e)
                showToast("Google sign in failed")
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}