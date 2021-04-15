package com.data.remote

import com.data.remote.entity.respond.BaseRespond
import com.data.remote.entity.respond.news.NewsNetworkEntity

interface RemoteDataSource {
   suspend fun fetchHotNews(selectedTitle: String): BaseRespond<NewsNetworkEntity>
}