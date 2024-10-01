package com.mynote.push

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mynote.R
import com.project.app.utils.AppConstant
import com.project.app.utils.PrefUtils
import com.project.app.utils.extension.showToast
import com.project.app.utils.extension.urlToBitmap
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var prefUtils: PrefUtils
    private val TAG = "MyFirebaseMessagingServ"
    var stateNoti = true

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: refreshed token : $token")
    }

    override fun onCreate() {
        super.onCreate()
        stateNoti = prefUtils.getPushNotification()
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        val notificationManager = getSystemService(NotificationManager::class.java)


        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")



            val notificationTitle = remoteMessage.notification?.title ?: "Default Title"
            val notificationBody = remoteMessage.notification?.body ?: "Default Body"
            val notificationImage = remoteMessage.notification?.imageUrl ?: "Default Url"

            val color = remoteMessage.data["color"] ?: "#FFFFFF"
            Log.d(TAG, "onMessageReceived: color = $color")

            val image = urlToBitmap(notificationImage.toString())
            Log.d(TAG, "onMessageReceived: bitmap = $image")
            val builder = NotificationCompat.Builder(this, AppConstant.CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setAutoCancel(true)
                .setLargeIcon(image)
                .setColor(Color.parseColor(color))
                .setPriority(NotificationCompat.PRIORITY_MAX)

            if (stateNoti){
                Log.d(TAG, "onMessageReceived: notification On")
                notificationManager.notify(0, builder.build())
            }else{
                Log.d(TAG, "onMessageReceived: notification Off")

            }
        }


        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            Log.d(TAG, "Message Notification image: ${it.imageUrl}")

        }
    }

}