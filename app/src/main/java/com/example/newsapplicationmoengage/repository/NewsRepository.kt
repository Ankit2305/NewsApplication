package com.example.newsapplicationmoengage.repository

import com.example.newsapplicationmoengage.constant.Endpoints
import com.example.newsapplicationmoengage.model.NewsWrapper
import com.example.newsapplicationmoengage.remote.ApiResponseObject
import com.example.newsapplicationmoengage.remote.GetApiResponse
import com.example.newsapplicationmoengage.remote.RemoteApiFetcher

/**
 * Repository class responsible for fetching news data from the API.
 */
class NewsRepository {

    /**
     * Fetches all news articles from the API.
     *
     * @return An [ApiResponseObject] representing the result of the API request.
     */
    suspend fun fetchAllNews(): ApiResponseObject<NewsWrapper> {
        return GetApiResponse.getApiResponse(Endpoints.NEWS_ENDPOINT)
    }

}