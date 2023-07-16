package com.example.newsapplicationmoengage.model

import com.example.newsapplicationmoengage.constant.ViewType

data class News(
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String,
    val viewType: Int = ViewType.NEWS
)
