package com.example.newsapplicationmoengage.constant

import com.example.newsapplicationmoengage.model.News

object ViewType {
    const val NEWS = 0
    const val SHIMMER = 1

    fun getNewShimmeringModel(): News {
        return News(
            author = "",
            title = "",
            description = "",
            url = "",
            urlToImage = "",
            publishedAt = "",
            content = "",
            viewType = ViewType.SHIMMER
        )
    }
}