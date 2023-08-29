package com.example.newsapplicationmoengage.helper

import android.content.Context
import android.util.Log
import com.example.newsapplicationmoengage.model.User
import com.google.gson.JsonArray

object SharedPreferencesHelper {

    const val FILE_NAME = "com.example.newapplicationmoengage.shared_pref_news"
    const val LOGGED_IN_USER = "LOGGED_IN_USER"
    const val LOCAL_USERS = "LOCAL_USERS"

    fun setProperty(context: Context, key: String, value: String?) {
        val sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getProperty(context: Context, key: String): String? {
        val sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }

    fun addUser(context: Context, user: User) {
        val users = getAllUsers(context)
        val mutableUser = users.toMutableList().apply { add(user) }
        Log.i("Login_AddUser", mutableUser.toJson())
        setProperty(context, LOCAL_USERS, mutableUser.toJson())
    }

    fun getAllUsers(context: Context): List<User> {
        val usersArrayJson = getProperty(context, LOCAL_USERS)
        val usersArray: JsonArray? = usersArrayJson?.parseJson()
        return usersArray?.map {
            it.toJson().parseJson<User>() ?: User()
        }?.filter {
            it.userId.isNotBlank()
        } ?: emptyList<User>()
    }

    fun clearUsers(context: Context) {
        setProperty(context, LOCAL_USERS, "")
    }
}