package com.example.newsapplicationmoengage.helper.login

import android.content.Context
import android.util.Log
import com.example.newsapplicationmoengage.helper.SharedPreferencesHelper
import com.example.newsapplicationmoengage.helper.toJson
import com.example.newsapplicationmoengage.model.User

class LocalLogin(private val context: Context): ILogin{

    override fun login(
        userLoginId: String,
        password: String,
        onLoginSuccess: (userId: String) -> Unit,
        onLoginFailed: () -> Unit
    ) {
        val users = SharedPreferencesHelper.getAllUsers(context = context)
        val user = users.find {
            it.userLoginId == userLoginId
        }
        if(user?.password == password) {
            SharedPreferencesHelper.setProperty(context, SharedPreferencesHelper.LOGGED_IN_USER, user.toJson())
            onLoginSuccess.invoke(user.userId)
        } else {
            onLoginFailed.invoke()
        }
    }

    override fun logout(onLogoutComplete: () -> Unit) {
        SharedPreferencesHelper.setProperty(context, SharedPreferencesHelper.LOGGED_IN_USER, null)
        onLogoutComplete.invoke()
    }


    companion object {
        fun initializeLocalLogin(context: Context) {
            val users = listOf(
                User(
                    userId = "1234",
                    userLoginId = "ankit",
                    userName = "Ankit Kumar",
                    phoneNumber = "1234567890",
                    email = "ankit.kumar@moengage.com",
                    password = "pass1234"
                    ),
                User(
                    userId = "1235",
                    userLoginId = "root",
                    userName = "Root User",
                    phoneNumber = "1234567890",
                    email = "root@moengage.com",
                    password = "root1234"
                ),
                User(
                    userId = "1236",
                    userLoginId = "admin",
                    userName = "Admin User",
                    phoneNumber = "1234567890",
                    email = "admin@moengage.com",
                    password = "admin1234"
                ),
            )

            SharedPreferencesHelper.clearUsers(context)
            users.forEach {
                SharedPreferencesHelper.addUser(context, it)
            }

            Log.i("LocalLoginTag", "${SharedPreferencesHelper.getAllUsers(context)}")
        }
    }

}