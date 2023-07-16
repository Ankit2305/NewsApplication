package com.example.newsapplicationmoengage

import android.app.Application
import com.example.newsapplicationmoengage.di.appModule
import com.example.newsapplicationmoengage.helper.addFCMTokenListener
import com.google.firebase.FirebaseApp
import org.koin.core.context.startKoin

class NewsApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Register FCM token listener
        addFCMTokenListener(this)

        // Configure Koin modules
        startKoin {
            modules(appModule)
        }
    }
}