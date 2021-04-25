package com.data.remote

import android.app.Application
import com.crypto.TNCrypto
import com.data.di.DataModule
import com.data.remote.entity.HEAD_LINES
import com.data.remote.entity.request.NewsAPIRequest
import com.data.remote.entity.respond.BaseRespond
import com.data.remote.entity.respond.news.ArticleDto
import com.data.remote.entity.respond.news.NewsNetworkEntity
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
        private val apiService: APIService,
        private val app: Application,
        @DataModule.ServerPublicCertificate private val serverPublicCertificate: String,
        private val tnCrypto: TNCrypto
) : RemoteDataSource {
    override suspend fun fetchHotNews(
        selectedTitle: String
    ): Flow<List<ArticleDto>>  = flow {
        val request = NewsAPIRequest(selectedTitle)

        val apiResult: BaseRespond<NewsNetworkEntity> = WebApiRequestHandler()
            .requestRestApiAsync(
                apiService
                , app
                , serverPublicCertificate
                , tnCrypto
                , request
                , HEAD_LINES
            )
        if (apiResult.isSuccess) {
            val articleDtoList = apiResult.data?.articles
            val cleanArticleList =  articleDtoList?.filter { it.author != null }
            emit(cleanArticleList!!)
        } else {
            emit(emptyList())
        }
    }
}