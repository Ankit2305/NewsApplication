package com.example.newsapplicationmoengage.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.newsapplicationmoengage.MainActivity
import com.example.newsapplicationmoengage.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.pushbase.MoEPushHelper
import kotlin.random.Random

class NewsFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
//        MoEFireBaseHelper.getInstance().passPushToken(applicationContext,token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        var handleNotification = true

        // Handle the received FCM message here
        if(MoEPushHelper.getInstance().isFromMoEngagePlatform(remoteMessage.data)){
            Log.i("NewsFMSTag", "onMessageReceived ${remoteMessage.data}")

            //Pass Data payload to MoeEngage SDK
//            MoEFireBaseHelper.getInstance().passPushPayload(applicationContext, remoteMessage.data)
//            handleNotification = false

            //Handling MoEngage Push manually
            val payload = remoteMessage.data
            remoteMessage.data.put("title", payload.getValue("gcm_title"))
            remoteMessage.data.put("message", payload.getValue("gcm_alert"))
            MoEPushHelper.getInstance().logNotificationReceived(applicationContext, remoteMessage.data)
            handleNotification = !MoEPushHelper.getInstance().isSilentPush(remoteMessage.data)
        }

        if (handleNotification && remoteMessage.data.isNotEmpty()) {
            // Process the data payload
            val data = remoteMessage.data
            // Extract necessary values from the data payload and handle them accordingly
            val title = data["title"]
            val message = data["message"]

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("logNotificationOnClick", true)
                data.forEach { (key, value) ->
                    putExtra(key, value)
                }
            }
            val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE)

            // Show the notification or handle the data payload as needed
            val notificationBuilder = NotificationCompat.Builder(this, "com.example.newsapplicationmoengage")
                .setSmallIcon(R.drawable.ic_ascending)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)


            val notificationManager = NotificationManagerCompat.from(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(NotificationChannel("com.example.newsapplicationmoengage", "FCM Channel", NotificationManager.IMPORTANCE_HIGH))
            }
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notificationManager.notify(999999999, notificationBuilder.build())
        }
    }

}