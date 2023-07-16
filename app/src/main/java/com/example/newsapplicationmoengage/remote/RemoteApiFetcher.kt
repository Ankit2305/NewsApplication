package com.example.newsapplicationmoengage.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


/**
 * Object responsible for fetching data from a remote API.
 */
object RemoteApiFetcher {

    /**
     * Fetches data from the specified remote API URL and returns the response as a string.
     *
     * @param apiUrl The URL of the remote API.
     * @return The response data as a string, or null if an error occurred.
     */
    suspend fun fetchDataFromRemoteApi(apiUrl: String): String? = withContext(Dispatchers.IO) {
        try {
            // Create a URL object
            val url = URL(apiUrl)

            // Open a connection to the API
            val connection = url.openConnection() as HttpURLConnection

            // Set request method
            connection.requestMethod = "GET"

            // Set any headers required by the API (e.g., authentication)
            // connection.setRequestProperty("Authorization", "Bearer your_token_here")

            // Set timeouts (optional)
            connection.connectTimeout = 5000 // 5 seconds
            connection.readTimeout = 5000 // 5 seconds

            // Connect to the API
            connection.connect()

            // Check the response code
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response data
                val inputStream: InputStream = connection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                // Close the input stream and connection
                bufferedReader.close()
                inputStream.close()
                connection.disconnect()

                // Return the response as a string
                return@withContext response.toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return@withContext null
    }


}