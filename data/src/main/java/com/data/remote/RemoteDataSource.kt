package com.data.remote

import com.data.remote.entity.respond.news.ArticleDto
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
   suspend fun fetchHotNews(selectedTitle: String): Flow<List<ArticleDto>>
}