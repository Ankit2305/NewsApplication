package com.example.newsapplicationmoengage.helper

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.newsapplicationmoengage.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.moengage.firebase.MoEFireBaseHelper

/**
 * Adds a listener to retrieve and handle the Firebase Cloud Messaging (FCM) registration token.
 *
 * @param context The context of the application or activity.
 */
fun addFCMTokenListener(context: Context) {
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w("DebugTag", "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }

        // Get new FCM registration token
        val token = task.result

//        MoEFireBaseHelper.getInstance().passPushToken(context.applicationContext, token)

        val msg = "Token: $token"
        Log.d("DebugTag", msg)
    })
}