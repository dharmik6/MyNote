package com.mynote.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mynote.R
import com.project.app.utils.AppConstant
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltAndroidApp
class MyApp : Application() {

    companion object {
        lateinit var instance: MyApp

        fun getAppInstance(): MyApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("noti", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result


            Log.d("noti", token)
        })

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val descriptionText = "Channel for FCM notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(AppConstant.CHANNEL_ID, AppConstant.CHANNEL_NAME, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

    }

}