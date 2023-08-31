package com.example.newsapplicationmoengage

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.newsapplicationmoengage.di.appModule
import com.example.newsapplicationmoengage.helper.MoeEventHelper
import com.example.newsapplicationmoengage.helper.SharedPreferencesHelper
import com.example.newsapplicationmoengage.helper.addFCMTokenListener
import com.example.newsapplicationmoengage.helper.login.LocalLogin
import com.example.newsapplicationmoengage.moe.CustomPushMessageListener
import com.google.firebase.FirebaseApp
import com.moengage.core.DataCenter
import com.moengage.core.LogLevel
import com.moengage.core.MoECoreHelper
import com.moengage.core.MoEngage
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.config.FcmConfig
import com.moengage.core.config.LogConfig
import com.moengage.core.config.NotificationConfig
import com.moengage.core.enableAdIdTracking
import com.moengage.core.listeners.AppBackgroundListener
import com.moengage.core.model.AppBackgroundData
import com.moengage.core.model.AppStatus
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.pushbase.MoEPushHelper
import com.moengage.pushbase.listener.TokenAvailableListener
import com.moengage.pushbase.model.Token
import org.koin.core.context.startKoin

class NewsApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        //Initialize MoEngage Sdk on main thread
        initializeMoEngageSdk()

        // Register FCM token listener
        addFCMTokenListener(this)

        //Add Token Listener
        addTokenListener()

        //Send App Open Event
        sendAppOpenEvent()

        //Set Up Koin Modules
        setUpKoinDi()

        //Install and Update AppStatus in MoEngage
        setInstallOrUpdateAppStatus()

        //Enable Ad Id Tracking
        enableAdIdTracking(this)

        //Initialize Local Login Setup
        LocalLogin.initializeLocalLogin(this)

        //AppBackgroundListener
        MoECoreHelper.addAppBackgroundListener(object: AppBackgroundListener {
            override fun onAppBackground(context: Context, data: AppBackgroundData) {
                Log.i("ApplicationTag", "context: ${context::class.simpleName}, data: $data")
                MoeEventHelper.sendEvent(
                    context = context,
                    "App Close Custom",
                    "appBackgroundData" to data.toString()
                )
            }

        })

        //Add Custom Notification Listener
        MoEPushHelper.getInstance().registerMessageListener(CustomPushMessageListener())

        setUpNotificationChannelsForMoEngage()

    }

    private fun setInstallOrUpdateAppStatus() {
        val appVersion = BuildConfig.VERSION_CODE.toString()
        val previousVersionCode = SharedPreferencesHelper
            .getProperty(this, "appVersionCode")

        if(previousVersionCode == null) {
            Log.i("NewApplicationTag", "MoEngage AppStatus = AppStatus.INSTALL")
            MoEAnalyticsHelper.setAppStatus(this, AppStatus.INSTALL)
            SharedPreferencesHelper.setProperty(this, "appVersionCode", appVersion)
        } else if(previousVersionCode < appVersion) {
            Log.i("NewApplicationTag", "MoEngage AppStatus = AppStatus.UPDATE")
            MoEAnalyticsHelper.setAppStatus(this, AppStatus.UPDATE)
        }
    }

    private fun sendAppOpenEvent() {
        MoeEventHelper.sendEvent(
            context = this,
            eventName = "App Open Custom",
            "timeStamp" to System.currentTimeMillis()
        )
    }

    private fun setUpNotificationChannelsForMoEngage() {
        MoEPushHelper.getInstance().setUpNotificationChannels(this)

        //	MoEPushHelper.getInstance().requestPushPermission(activity)
        // 	MoEPushHelper.getInstance().navigateToSettings(activity)
    }

    private fun addTokenListener() {
        MoEFireBaseHelper.getInstance().addTokenListener { token ->
            Log.i(
                "NewsApplicationTag",
                "Token Available: $token"
            )
        }
    }

    private fun initializeMoEngageSdk() {
        val moEngage = MoEngage.Builder(this, "EAUBZROL4WEPUSJDS814PDQO", DataCenter.DATA_CENTER_1)
            .configureNotificationMetaData(
                NotificationConfig(
                    smallIcon = R.drawable.ic_stat_name,
                    largeIcon = R.mipmap.ic_launcher,
                    notificationColor = R.color.moengage_primary,
                    isMultipleNotificationInDrawerEnabled = true
                )
            )
//            .configureFcm(FcmConfig(false))
            .configureLogs(LogConfig(level = LogLevel.VERBOSE, true))
            .build()
        MoEngage.initialiseDefaultInstance(moEngage)
    }

    private fun setUpKoinDi() {
        // Configure Koin modules
        startKoin {
            modules(appModule)
        }
    }
}