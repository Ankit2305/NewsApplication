package com.example.newsapplicationmoengage.model

data class NewsWrapper(
    val status: String,
    val articles: List<News>
)