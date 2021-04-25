package com.domain.repository

import com.data.remote.RemoteDataSource
import com.data.remote.entity.respond.BaseRespond
import com.data.remote.entity.respond.news.ArticleDto
import com.data.remote.entity.respond.news.NewsNetworkEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RemoteRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    suspend fun getHotNews(
        selectedTitle: String
    ): Flow<List<ArticleDto>> {
      return remoteDataSource.fetchHotNews(selectedTitle)
    }

}