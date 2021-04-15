package com.domain.repository

import com.data.remote.RemoteDataSource
import com.data.remote.entity.respond.BaseRespond
import com.data.remote.entity.respond.news.NewsNetworkEntity
import javax.inject.Inject


class RemoteRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    suspend fun getHotNews(
        selectedTitle: String
    ): BaseRespond<NewsNetworkEntity> {
      return remoteDataSource.fetchHotNews(selectedTitle)
    }

}