package com.project.app.utils

import android.util.Patterns
import java.util.regex.Pattern

class ValidationUtils {
    companion object {
        fun isValidPassword(password: String): Boolean {
            val passwordpattern = Pattern.compile(
                "^" +
                        "(?=.*[a-zA-Z0-9])" +      //any letter
                        "(?=\\S+$)" +           //no white spaces
                        ".{6,}" +               //at least 6 characters
                        "$"
            )
            return passwordpattern.matcher(password).matches()

        }

        fun isValidEmail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isEmptyFiled(data: String): Boolean {
            return data.isEmpty()
        }

        fun getGenderParams(value: String): String {
            var gender = "M"
            when (value) {
                "Male" -> {
                    gender = "M"
                }
                "Female" -> {
                    gender = "F"
                }
                "No" -> {
                    gender = "N"
                }
            }
            return gender
        }


    }
}