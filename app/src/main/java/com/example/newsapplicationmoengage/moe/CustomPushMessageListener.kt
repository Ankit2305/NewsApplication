package com.example.newsapplicationmoengage.moe

import android.app.Activity
import android.app.Notification
import android.app.Notification.Action
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.moengage.pushbase.model.NotificationPayload
import com.moengage.pushbase.model.action.DismissAction
import com.moengage.pushbase.push.PushMessageListener

class CustomPushMessageListener: PushMessageListener() {

    override fun onCreateNotification(
        context: Context,
        notificationPayload: NotificationPayload
    ): NotificationCompat.Builder {
        val moeNotificationBuilder = super.onCreateNotification(context, notificationPayload)
//        moeNotificationBuilder
        Log.i("CustomPMLTag", "${notificationPayload.payload}")
        val url = notificationPayload.payload.getString("showGoogleButton")
        Log.i("CustomPMLTag", "$url")
        if(url != null) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            moeNotificationBuilder.addAction(0, "Open Url", pendingIntent)
        }
        return moeNotificationBuilder
    }

    override fun isNotificationRequired(context: Context, payload: Bundle): Boolean {
        val canShowNotification = super.isNotificationRequired(context, payload)
        if(!canShowNotification) return false
        Log.i("CustomPMLTag", "${payload.getString("hideNotification")}")

        val hideNotification = payload.getString("hideNotification") == "true"
        if(hideNotification) return false

        return true
    }

    override fun getRedirectIntent(context: Context): Intent {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://www.moengage.com")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return intent
    }

    override fun onNotificationClick(activity: Activity, payload: Bundle) {
        val url = payload.getString("showGoogleButton")
        Log.i("CustomPMSTag", "onNotificationClick: ${url}")
        if(url != null) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            activity.startActivity(intent)
        } else {
            super.onNotificationClick(activity, payload)
        }
    }

    override fun handleCustomAction(context: Context, payload: String) {
        Log.i("CustomPMLTag", "handleCustomAction: $payload")
        if(payload.isNotBlank()) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(payload)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } else {
            super.handleCustomAction(context, payload)
        }
    }

    override fun customizeNotification(
        notification: Notification,
        context: Context,
        payload: Bundle
    ) {
//        notification.
    }
}