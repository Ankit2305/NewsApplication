package com.example.newsapplicationmoengage.helper

import java.text.SimpleDateFormat
import java.util.*


/**
 * Object responsible for parsing and formatting dates.
 */
object DateParser {

    /**
     * Parses the provided date string and returns a formatted date representation.
     *
     * @param dateString The date string to parse.
     * @return The formatted date representation.
     */

    fun parseDate(millis: Long): String {
        return try {
            val date = Date(millis)
            formattedDate(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun formattedDate(date: Date): String {
        // Get the current date and time
        val now = Date()

        // Calculate the difference between the parsed date and the current date in milliseconds
        val diffInMillis = now.time - date.time

        // Determine the appropriate format based on the time difference
        return when {
            diffInMillis >= 24 * 60 * 60 * 1000 -> {
                // If the difference is greater than or equal to 24 hours, format as "d MMM yyyy, h:mm a"
                val outputFormat = SimpleDateFormat("d MMM yyyy, h:mm a", Locale.getDefault())
                outputFormat.format(date)
            }
            diffInMillis >= 2 * 60 * 60 * 1000 -> {
                // If the difference is greater than or equal to 2 hours, format as "h:mm a"
                val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                outputFormat.format(date)
            }
            diffInMillis >= 60 * 60 * 1000 -> {
                // If the difference is greater than or equal to 1 hour, format as "x hours ago"
                val hours = diffInMillis / (60 * 60 * 1000)
                "$hours hours ago"
            }
            else -> {
                // If the difference is less than 1 hour, format as "x minutes ago"
                val minutes = diffInMillis / (60 * 1000)
                "$minutes minutes ago"
            }
        }
    }
    fun parseDate(dateString: String): String {
        try {
            // Create an input date format and set the time zone to UTC
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            // Parse the date string
            val date = inputFormat.parse(dateString) ?: Date()

            return formattedDate(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }
}