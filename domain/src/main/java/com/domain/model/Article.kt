package com.domain.model

data class Article  (
    val author: String,
    val title: String,
    val url: String,
    val urlToImage: String? = null,
    val publishedAt: String,
    val source: String
)