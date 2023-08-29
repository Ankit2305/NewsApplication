package com.example.newsapplicationmoengage.helper

import com.google.gson.Gson
import com.google.gson.JsonArray

object GsonHelper {

    val gson = Gson()

    fun toJson(obj: Any?): String {
        return try {
            gson.toJson(obj)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    inline fun <reified T> parseFromJson(json: String) : T? {
        return try {
            gson.fromJson(json, T::class.java)
        } catch (e: Exception) {
            null
        }
    }

}