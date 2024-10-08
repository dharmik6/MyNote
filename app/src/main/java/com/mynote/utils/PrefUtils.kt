package com.project.app.utils

import android.content.SharedPreferences
import com.google.gson.Gson

class PrefUtils(private val sharedPreferences: SharedPreferences) {

    fun saveAuthToken(token: String?) {
        sharedPreferences.edit().putString(AppConstant.CONSTANT_AUTH, token).apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(AppConstant.CONSTANT_AUTH, null)
    }

    // save over all app text size
    fun saveTextSize(size: Int) {
        sharedPreferences.edit().putInt(AppConstant.TEXT_SIZE, size).apply()
    }

    fun getTextSize(): Int {
        return sharedPreferences.getInt(AppConstant.TEXT_SIZE, 1)
    }
    // save push notification from FCM
    fun savePushNotification(state : Boolean) {
        sharedPreferences.edit().putBoolean(AppConstant.PUSH_NOTIFICATION, state).apply()
    }

    fun getPushNotification(): Boolean {
        return sharedPreferences.getBoolean(AppConstant.PUSH_NOTIFICATION, true)
    }



    fun isUserLoggedIn(): Boolean {
        return !getAuthToken().isNullOrEmpty()
    }

    fun clearPref() {
        sharedPreferences.edit().clear().apply()
    }

}