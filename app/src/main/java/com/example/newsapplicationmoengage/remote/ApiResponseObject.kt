package com.example.newsapplicationmoengage.remote

/**
 * Sealed class representing the result of an API response.
 */
sealed class ApiResponseObject<out T: Any?> {
    /**
     * Represents a successful API response with the [data] payload.
     *
     * @property data The response data.
     */
    class Success<T>(val data: T?): ApiResponseObject<T>()

    /**
     * Represents an error response with the specified [message] and [exception].
     *
     * @property message The error message.
     * @property exception The exception associated with the error, if applicable.
     */
    class Error(val message: String,val exception: Exception): ApiResponseObject<Nothing>()
}