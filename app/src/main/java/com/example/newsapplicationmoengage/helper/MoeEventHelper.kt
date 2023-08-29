package com.example.newsapplicationmoengage.helper

import android.content.Context
import com.example.newsapplicationmoengage.model.User
import com.moengage.core.MoECoreHelper
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.internal.USER_ATTRIBUTE_USER_EMAIL
import com.moengage.core.internal.USER_ATTRIBUTE_USER_MOBILE
import com.moengage.core.internal.USER_ATTRIBUTE_USER_NAME
import com.moengage.core.internal.model.Attribute

object MoeEventHelper {
    fun sendEvent(context: Context, eventName: String, vararg attributes: Pair<String, Any?>) {
        val properties = Properties()
        attributes.forEach { ( key, value ) ->
            properties.addAttribute(key, value)
        }
        MoEAnalyticsHelper.trackEvent(context, eventName, properties)
    }

    fun setMoEUserAttributes(context: Context) {
        val loggedInUser = SharedPreferencesHelper.getProperty(context, SharedPreferencesHelper.LOGGED_IN_USER)?.parseJson<User>()
        loggedInUser?.let { user ->
            MoEAnalyticsHelper.setUserAttribute(context, USER_ATTRIBUTE_USER_NAME, user.userName)
            MoEAnalyticsHelper.setUserAttribute(context, USER_ATTRIBUTE_USER_EMAIL, user.email)
            MoEAnalyticsHelper.setUserAttribute(context, USER_ATTRIBUTE_USER_MOBILE, user.phoneNumber)

            MoEAnalyticsHelper.setAlias(context, "user_${user.userLoginId}")
            MoEAnalyticsHelper.setAlias(context, user.userId)
        }

    }
}