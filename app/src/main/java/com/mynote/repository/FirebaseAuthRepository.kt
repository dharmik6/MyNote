package com.mynote.repository

import android.net.Uri
import android.util.Log
import com.dottore.app.network.exceptions.ValidationException
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mynote.data.param.NotesParam
import com.mynote.data.param.UserDataParam
import com.mynote.utils.resource.FireBaseResponseHandler
import com.project.app.utils.ErrorBean
import com.project.app.utils.resource.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseAuthRepository @Inject constructor(private val responseHandler: FireBaseResponseHandler) {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private val TAG = "FirebaseAuthRepository"

    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<FirebaseUser?>> = flow {
        emit(Resource.loading<FirebaseUser?>())

        try {
            auth = Firebase.auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()

            authResult.user!!.getIdToken(true).addOnSuccessListener {
                Log.d(TAG, "token = ${it.token}")
            }

            emit(Resource.success(authResult.user))

        } catch (e: Exception) {
            emit(responseHandler.handleFirebaseError(e))
        }
    }.catch { e ->
        emit(responseHandler.handleFirebaseError(ValidationException(e.message)))
    }


    suspend fun insertOrUpdateUserData(
        userId: String,
        userData: UserDataParam
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading<Boolean>())

        try {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(userId)

            // Insert or update user data
            userRef.set(userData)
                .await()

            emit(Resource.success(true))

        } catch (e: Exception) {
            emit(responseHandler.handleFirebaseError(e))
        }
    }.catch {
        emit(responseHandler.handleFirebaseError(ValidationException(it.message)))
    }

    suspend fun updateUserdata(
        userId: String,
        userData: UserDataParam
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading())

        try {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(userId)
            userRef.update("name", userData.name)
            if (userData.email != null) {
                auth = Firebase.auth
                if (auth.currentUser != null) {
                    auth.currentUser!!.updateEmail(userData.email).addOnSuccessListener {
                        Log.d(TAG, "updateUserdata: update email ")
                        userRef.update("email", userData.email)
                    }.addOnFailureListener { e ->
                        Log.d(TAG, "updateUserdata: failed email")
                    }
                }

            }

            userRef.update("profileImage", userData.profileImage)
                .await()

            emit(Resource.success(true))

        } catch (e: Exception) {
            emit(responseHandler.handleFirebaseError(e))
        }
    }.catch {
        emit(responseHandler.handleFirebaseError(ValidationException(it.message)))
    }

    suspend fun getUserData(userId: String): Flow<Resource<UserDataParam>> = flow {
        emit(Resource.loading())

        var userData: UserDataParam? = null
        try {
            val docRef = db.collection("users").document(userId)
            docRef.get().addOnSuccessListener { documentSnapshot ->
                userData = documentSnapshot.toObject<UserDataParam>()

            }.await()
            if (userData != null) {
                emit(Resource.success(userData))
            }
        } catch (e: Exception) {
            emit(responseHandler.handleFirebaseError(e))
        }
    }.catch {
        emit(responseHandler.handleFirebaseError(ValidationException(it.message)))
    }


    suspend fun signInUserWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<FirebaseUser?>> = flow {
        emit(Resource.loading())

        try {
            auth = Firebase.auth
            val authResult = auth.signInWithEmailAndPassword(email, password).await()

            emit(Resource.success(authResult.user))

        } catch (e: Exception) {
            Log.d(TAG, "signInUserWithEmailAndPassword: exception message = ${e}")
            emit(responseHandler.handleFirebaseError(e))
        }
    }.catch { e ->
        emit(responseHandler.handleFirebaseError(ValidationException(e.message)))
    }

    suspend fun resetPassword(
        email: String
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading())

        try {
            auth = Firebase.auth
            auth.sendPasswordResetEmail(email).await()
            emit(Resource.success(true))

        } catch (e: Exception) {
            Log.d(TAG, "signInUserWithEmailAndPassword: exception message = ${e}")
            emit(responseHandler.handleFirebaseError(e))
        }
    }.catch { e ->
        emit(responseHandler.handleFirebaseError(ValidationException(e.message)))
    }

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        retypeNewPassword: String
    ): Flow<Resource<FirebaseUser?>> = flow {
        emit(Resource.loading())

        try {
            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {

                val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
                user.reauthenticate(credential).await()

                // Step 2: Validate new password and retype password
                if (newPassword != retypeNewPassword) {
                    emit(Resource.error<FirebaseUser?>(ErrorBean(errorMessage = "New password and Retyped password do not match")))
                    return@flow
                }

                // Step 3: Update password
                user.updatePassword(newPassword).await()
                emit(Resource.success(user))
            } else {
                emit(Resource.error<FirebaseUser?>(ErrorBean(errorMessage = "User not authenticated")))
            }

        } catch (e: FirebaseAuthException) {
            // Handle Firebase authentication-specific errors
            Log.e(TAG, "changePassword: $e")
            emit(responseHandler.handleFirebaseError(e))
        } catch (e: Exception) {
            // Handle general exceptions
            Log.e(TAG, "changePassword: $e")
            emit(responseHandler.handleFirebaseError(e))
        }
    }.catch { e ->
        Log.e(TAG, "changePassword: $e")
        emit(responseHandler.handleFirebaseError(ValidationException(e.message)))
    }


    suspend fun sendNoteData(
        userId: String,
        noteData: NotesParam
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading<Boolean>())

        try {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(userId).collection("notes").document()
            noteData.noteId = userRef.id

            // Debugging: Check the map content
            val noteDataMap = noteData.toMap()
            Log.d("SendNoteData", "noteDataMap: $noteDataMap")

            // Insert or update user data
            userRef.set(noteDataMap) // Make sure to use the map here
                .await()

            emit(Resource.success(true))

        } catch (e: Exception) {
            emit(responseHandler.handleFirebaseError(e))
        }
    }.catch {
        emit(responseHandler.handleFirebaseError(ValidationException(it.message)))
    }


    suspend fun updateNoteData(
        userId: String,
        noteId: String,
        noteData: NotesParam
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading<Boolean>())

        try {
            val db = FirebaseFirestore.getInstance()
            val userRef =
                db.collection("users").document(userId).collection("notes").document(noteId)
            noteData.noteId = userRef.id
            val noteDataMap = noteData.toMap()
            userRef.update(noteDataMap)
                .await()

            emit(Resource.success(true))

        } catch (e: Exception) {
            emit(responseHandler.handleFirebaseError(e))
        }
    }.catch {
        emit(responseHandler.handleFirebaseError(ValidationException(it.message)))
    }

    suspend fun deleteNotes(
        userId: String,
        noteData: List<NotesParam>
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading<Boolean>())

        try {
            val db = FirebaseFirestore.getInstance()

            // Parallel deletion of notes using coroutineScope
            coroutineScope {
                noteData.map { note ->
                    async {
                        note.noteId?.let {
                            val userRef = db.collection("users")
                                .document(userId)
                                .collection("notes")
                                .document(it)

                            userRef.delete().await()
                        } ?: throw IllegalArgumentException("Note ID cannot be null")
                    }
                }
            }

            emit(Resource.success(true))

        } catch (e: Exception) {
            emit(responseHandler.handleFirebaseError(e))
        }
    }.catch {
        emit(responseHandler.handleFirebaseError(ValidationException(it.message)))
    }


    suspend fun updateNotes(
        userId: String,
        noteData: List<NotesParam>
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading<Boolean>())

        try {
            val db = FirebaseFirestore.getInstance()

            // Parallel updating of notes using coroutineScope
            coroutineScope {
                noteData.map { note ->
                    async {
                        note.noteId?.let {
                            val userRef = db.collection("users")
                                .document(userId)
                                .collection("notes")
                                .document(it)

                            val noteDataMap = note.toMap() // Convert note to map
                            userRef.update(noteDataMap) // Update the note in Firestore
                                .await()

                        } ?: throw IllegalArgumentException("Note ID cannot be null")
                    }
                }
            }

            emit(Resource.success(true))

        } catch (e: Exception) {
            emit(responseHandler.handleFirebaseError(e))
        }
    }.catch {
        emit(responseHandler.handleFirebaseError(ValidationException(it.message)))
    }


    suspend fun uploadImageAndGetUrl(imageUri: Uri): Flow<Resource<String>> = flow {
        emit(Resource.loading())

        // Ensure that the imageUri is valid
        if (imageUri.scheme != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val firestore = FirebaseFirestore.getInstance()

            try {
                // Create a reference to the location where you want to store the image
                val imageRef: StorageReference =
                    storageRef.child("images/${imageUri.lastPathSegment}")

                // Upload the image file
                val uploadTask = imageRef.putFile(imageUri).await()

                // Get the download URL of the uploaded image
                val imageUrl = uploadTask.storage.downloadUrl.await().toString()

                emit(Resource.success(imageUrl))
            } catch (e: Exception) {
                Log.e("UploadError", "Error uploading image", e)
                emit(Resource.error(ErrorBean(errorMessage = "Failed to upload image: ${e.message}"))) // Emit error message
            }
        } else {
            emit(Resource.error(ErrorBean(errorMessage = "Invalid image URI"))) // Handle invalid URI
        }
    }.catch { e ->
        Log.e("UploadError", "Caught exception during flow", e)
        emit(Resource.error(ErrorBean(errorMessage = "An error occurred: ${e.message}")))
    }


    suspend fun getNotesList(
        userId: String,
    ): Flow<Resource<List<NotesParam>>> = flow {
        emit(Resource.loading())

        try {
            val db = FirebaseFirestore.getInstance()
            val userNotesRef = db.collection("users").document(userId).collection("notes")

            val data = userNotesRef.get().await()

            val notesList = data.documents.mapNotNull { document ->
                document.toObject(NotesParam::class.java)
            }

            Log.d(TAG, "getNotesList: list = $notesList")
            emit(Resource.success(notesList))

        } catch (e: Exception) {
            emit(responseHandler.handleFirebaseError(e))
        }
    }.catch {
        emit(responseHandler.handleFirebaseError(ValidationException(it.message)))
    }


    suspend fun googleSignIn(
        idToken: String,
    ): Flow<Resource<FirebaseUser?>> = flow {
        emit(Resource.loading())

        try {
            auth = Firebase.auth
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(firebaseCredential)
                .await()
            emit(Resource.success(result.user))

        } catch (e: Exception) {
            Log.d(TAG, "signInUserWithEmailAndPassword: exception message = ${e}")
            emit(responseHandler.handleFirebaseError(e))
        }
    }.catch { e ->
        emit(responseHandler.handleFirebaseError(ValidationException(e.message)))
    }





}