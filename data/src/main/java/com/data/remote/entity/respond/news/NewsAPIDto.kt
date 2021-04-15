package com.data.remote.entity.respond.news

import com.google.gson.annotations.SerializedName

data class NewsNetworkEntity (
    val status: String,
    val totalResults: Long,
    val articles: List<ArticleDto>
)

data class ArticleDto  (
        @SerializedName("source")
        val source: SourceNetworkEntity,
        @SerializedName("author")
        val author: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("urlToImage")
        val urlToImage: String? = null,
        @SerializedName("publishedAt")
        val publishedAt: String,
        @SerializedName("content")
        val content: String
)

data class SourceNetworkEntity  (
        val id: String? = null,
        val name: String
)