package com.mynote.utils.resource

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.project.app.utils.ErrorBean
import com.project.app.utils.resource.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import retrofit2.Response

class FireBaseResponseHandler {

    fun <T> handleFirebaseError(
        exception: Exception,
        isAutoHandleError: Boolean = true
    ): Resource<T> {
        return if (isAutoHandleError) {
            when (exception) {
                is FirebaseAuthInvalidUserException -> {
                    when (exception.errorCode) {
                        "ERROR_USER_NOT_FOUND" -> {
                            Resource.error(
                                ErrorBean(
                                    errorCode = 404,
                                    errorMessage = "No account found with this email. Please sign up."
                                )
                            )
                        }
                        "ERROR_USER_DISABLED" -> {
                            Resource.error(
                                ErrorBean(
                                    errorCode = 403,
                                    errorMessage = "This account has been disabled. Contact support."
                                )
                            )
                        }
                        else -> {
                            Resource.error(
                                ErrorBean(
                                    errorCode = 401,
                                    errorMessage = exception.message ?: "Unknown error occurred."
                                )
                            )
                        }
                    }
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    when (exception.errorCode) {
                        "ERROR_INVALID_EMAIL" -> {
                            // Handle invalid email format
                            Resource.error(
                                ErrorBean(
                                    errorCode = 400,
                                    errorMessage = "The email address is not valid. Please check the format."
                                )
                            )
                        }
                        "ERROR_WRONG_PASSWORD" -> {
                            // Handle wrong password
                            Resource.error(
                                ErrorBean(
                                    errorCode = 401,
                                    errorMessage = "The password is incorrect. Please try again."
                                )
                            )
                        }
                        "ERROR_USER_NOT_FOUND" -> {
                            // Handle email not found
                            Resource.error(
                                ErrorBean(
                                    errorCode = 404,
                                    errorMessage = "No account found with this email. Please sign up."
                                )
                            )
                        }
                        else -> {
                            Resource.error(
                                ErrorBean(
                                    errorCode = 401,
                                    errorMessage = exception.message ?: "Invalid credentials provided."
                                )
                            )
                        }
                    }
                }
                is FirebaseAuthUserCollisionException -> {
                    Resource.error(
                        ErrorBean(
                            errorCode = 409,
                            errorMessage = "This email is already in use."
                        )
                    )
                }
                is FirebaseNetworkException -> {
                    Resource.error(
                        ErrorBean(
                            errorCode = 500,
                            errorMessage = "Network error. Please check your internet connection."
                        )
                    )
                }
                else -> {
                    Resource.error(
                        ErrorBean(
                            errorCode = -1,
                            errorMessage = exception.message ?: "Unknown Firebase error occurred."
                        )
                    )
                }
            }
        } else {
            Resource.error(
                ErrorBean(
                    errorCode = -1,
                    errorMessage = exception.message ?: "Unknown error occurred."
                )
            )
        }
    }
}
