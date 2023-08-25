package com.example.newsapplicationmoengage

import android.app.Application
import com.example.newsapplicationmoengage.di.appModule
//import com.example.newsapplicationmoengage.helper.addFCMTokenListener
import com.google.firebase.FirebaseApp
import com.moengage.core.DataCenter
import com.moengage.core.LogLevel
import com.moengage.core.MoEngage
import com.moengage.core.config.LogConfig
import org.koin.core.context.startKoin

class NewsApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Register FCM token listener
//        addFCMTokenListener(this)
        val moEngage = MoEngage.Builder(this, "EAUBZROL4WEPUSJDS814PDQO", DataCenter.DATA_CENTER_1)
            .configureLogs(LogConfig(level = LogLevel.VERBOSE, true))
            .build()
        MoEngage.initialiseDefaultInstance(moEngage)

        // Configure Koin modules
        startKoin {
            modules(appModule)
        }
    }
}