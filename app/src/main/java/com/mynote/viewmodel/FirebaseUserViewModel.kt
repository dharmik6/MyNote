package com.mynote.viewmodel

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.mynote.data.param.NotesParam
import com.mynote.data.param.UserDataParam
import com.mynote.repository.FirebaseAuthRepository
import com.project.app.utils.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseUserViewModel @Inject constructor(private val repository: FirebaseAuthRepository) :
    ViewModel() {

    private val TAG = "FirebaseUserViewModel"

    // registration user
    private val _createUserLiveData = MutableLiveData<Resource<FirebaseUser?>>()
    val createUserLiveData: LiveData<Resource<FirebaseUser?>> get() = _createUserLiveData

    fun createUser(email: String, password: String) {
        viewModelScope.launch {
            repository.createUserWithEmailAndPassword(email, password).collect { state ->
                _createUserLiveData.value = state

            }
        }
    }


    // login user
    private val _signInUserLiveData = MutableLiveData<Resource<FirebaseUser?>>()
    val signInUserLiveData: LiveData<Resource<FirebaseUser?>> get() = _signInUserLiveData

    fun signInUser(email: String, password: String) {
        viewModelScope.launch {
            repository.signInUserWithEmailAndPassword(email, password).collect { state ->
                _signInUserLiveData.value = state

            }
        }
    }

    // insert or update user data
    private val _insertOrUpdateUserLiveData = MutableLiveData<Resource<Boolean>>()
    val insertOrUpdateUserLiveData: LiveData<Resource<Boolean>> get() = _insertOrUpdateUserLiveData

    fun insertOrUpdateUser(userId: String, userData: UserDataParam) {
        viewModelScope.launch {
            Log.d(TAG, "insertOrUpdateUser: userId = $userId , userdata = $userData")
            repository.insertOrUpdateUserData(userId = userId, userData).collect { state ->
                _insertOrUpdateUserLiveData.value = state

            }
        }
    }

    // insert or update user data
    private val _updateUserLiveData = MutableLiveData<Resource<Boolean>>()
    val updateUserLiveData: LiveData<Resource<Boolean>> get() = _updateUserLiveData

    fun updateUser(userId: String, userData: UserDataParam) {
        viewModelScope.launch {
            Log.d(TAG, "insertOrUpdateUser: userId = $userId , userdata = $userData")
            repository.updateUserdata(userId = userId, userData).collect { state ->
                _updateUserLiveData.value = state

            }
        }
    }

    // reset email password
    private val _resetPasswordLiveData = MutableLiveData<Resource<Boolean>>()
    val resetPasswordLiveData: LiveData<Resource<Boolean>> get() = _resetPasswordLiveData

    fun resetPassword(email: String) {
        viewModelScope.launch {
            repository.resetPassword(email).collect { state ->
                _resetPasswordLiveData.value = state

            }
        }
    }

    // get user data
    private val _getUserDataLiveData = MutableLiveData<Resource<UserDataParam>>()
    val getUserDataLiveData: LiveData<Resource<UserDataParam>> get() = _getUserDataLiveData

    fun getUserData(userId: String) {
        viewModelScope.launch {
            repository.getUserData(userId).collect { state ->
                _getUserDataLiveData.value = state

            }
        }
    }

    // change password
    private val _changePasswordLiveData = MutableLiveData<Resource<FirebaseUser?>>()
    val changePasswordLiveData: LiveData<Resource<FirebaseUser?>> get() = _changePasswordLiveData

    fun changePassword(newPassword: String, currentPassword: String, retypePassword: String) {
        viewModelScope.launch {
            repository.changePassword(
                currentPassword = currentPassword,
                newPassword = newPassword,
                retypeNewPassword = retypePassword
            ).collect { state ->
                _changePasswordLiveData.value = state

            }
        }
    }

    // save notes
    private val _sendNoteLiveData = MutableLiveData<Resource<Boolean>>()
    val sendNoteLiveData: LiveData<Resource<Boolean>> get() = _sendNoteLiveData

    fun sendNoteData(userId: String, notesParam: NotesParam) {
        viewModelScope.launch {
            repository.sendNoteData(userId = userId, noteData = notesParam).collect { state ->
                _sendNoteLiveData.value = state

            }
        }
    }

    // save notes
    private val _updateNoteLiveData = MutableLiveData<Resource<Boolean>>()
    val updateNoteLiveData: LiveData<Resource<Boolean>> get() = _updateNoteLiveData

    fun updateNoteData(userId: String,noteId : String, notesParam: NotesParam) {
        viewModelScope.launch {
            repository.updateNoteData(userId = userId, noteId = noteId, noteData = notesParam).collect { state ->
                _updateNoteLiveData.value = state

            }
        }
    }

    // save image on firestore
    private val _uploadImageLiveData = MutableLiveData<Resource<String>>()
    val uploadImageLiveData: LiveData<Resource<String>> get() = _uploadImageLiveData

    fun uploadImage( imageUri: Uri) {
        viewModelScope.launch {
            repository.uploadImageAndGetUrl( imageUri = imageUri).collect { state ->
                _uploadImageLiveData.value = state

            }
        }
    }

    // save image on firestore
    private val _getNoteListLiveData = MutableLiveData<Resource<List<NotesParam>>>()
    val getNoteListLiveData: LiveData<Resource<List<NotesParam>>> get() = _getNoteListLiveData

    fun getNoteList(userId: String) {
        viewModelScope.launch {
            repository.getNotesList(userId = userId).collect { state ->
                _getNoteListLiveData.value = state

            }
        }
    }
    // delete notes on firestore
    private val _deleteNotesLiveData = MutableLiveData<Resource<Boolean>>()
    val deleteNotesLiveData: LiveData<Resource<Boolean>> get() = _deleteNotesLiveData

    fun deleteNotes(userId: String  , noteData : List<NotesParam>) {
        viewModelScope.launch {
            repository.deleteNotes(userId = userId , noteData = noteData ).collect { state ->
                _deleteNotesLiveData.value = state

            }
        }
    }

    // delete notes on firestore
    private val _updateNotesLiveData = MutableLiveData<Resource<Boolean>>()
    val updateNotesLiveData: LiveData<Resource<Boolean>> get() = _updateNotesLiveData

    fun updateNotes(userId: String  , noteData : List<NotesParam>) {
        viewModelScope.launch {
            repository.updateNotes(userId = userId , noteData = noteData ).collect { state ->
                _deleteNotesLiveData.value = state
            }
        }
    }

    // google sign in
    private val _googleSignInLiveData = MutableLiveData<Resource<FirebaseUser?>>()
    val googleSignInLiveData: LiveData<Resource<FirebaseUser?>> get() = _googleSignInLiveData

    fun googleSignIn(idToken: String) {
        viewModelScope.launch {
            repository.googleSignIn(idToken).collect { state ->
                _googleSignInLiveData.value = state
            }
        }
    }

}