package com.example.newsapplicationmoengage.remote

import android.util.Log
import com.example.newsapplicationmoengage.parser.ApiResponseParser

/**
 * Object responsible for performing API requests and parsing the response into [ApiResponseObject].
 */
object GetApiResponse {

    /**
     * Performs an API request to the specified URL and returns the parsed response as an [ApiResponseObject].
     *
     * @param url The URL to fetch the API response from.
     * @return An [ApiResponseObject] representing the parsed response.
     */
    suspend inline fun <reified T> getApiResponse(url: String): ApiResponseObject<T> {
        // Fetch the API response from the remote API
        val responseJson = RemoteApiFetcher.fetchDataFromRemoteApi(url)

        // Parse the API response using the ApiResponseParser
        return ApiResponseParser.parseApiResponse(responseJson)
    }

}