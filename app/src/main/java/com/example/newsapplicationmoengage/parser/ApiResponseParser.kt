package com.example.newsapplicationmoengage.parser

import com.example.newsapplicationmoengage.remote.ApiResponseObject
import com.google.gson.Gson
import java.lang.Exception

/**
 * Object responsible for parsing API responses into [ApiResponseObject]s.
 */
object ApiResponseParser  {

    /**
     * The Gson instance used for JSON deserialization.
     */
    val gson = Gson()


    /**
     * Parses the provided JSON string into an [ApiResponseObject] of type [T].
     *
     * @param json The JSON string to parse.
     * @return An [ApiResponseObject] representing the parsed response.
     */
    inline fun <reified T> parseApiResponse(json: String?): ApiResponseObject<T> {
        return try {
            val data = gson.fromJson(json, T::class.java)
            ApiResponseObject.Success(data)
        } catch (e: Exception) {
            ApiResponseObject.Error("Unable to parse response", Exception())
        }
    }
}