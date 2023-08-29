package com.example.newsapplicationmoengage.helper

import com.example.newsapplicationmoengage.parser.ApiResponseParser
import com.google.gson.JsonArray

fun Any?.toJson(): String {
    return GsonHelper.toJson(this)
}

//inline fun <reified T> MutableList<T>.toJsonArray(): String {
//    val array = JsonArray()
//    this.forEach {
//        array.add(it.toJson())
//    }
//    return array.toString()
//}

inline fun <reified T> String.parseJson(): T? {
    return GsonHelper.parseFromJson<T>(this)
}