package com.data.remote

import android.app.Application
import com.crypto.TNCrypto
import com.data.di.DataModule
import com.data.remote.entity.HEAD_LINES
import com.data.remote.entity.request.NewsAPIRequest
import com.data.remote.entity.respond.BaseRespond
import com.data.remote.entity.respond.news.NewsNetworkEntity
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
        private val apiService: APIService,
        private val app: Application,
        @DataModule.ServerPublicCertificate private val serverPublicCertificate: String,
        private val tnCrypto: TNCrypto
) : RemoteDataSource {
    override suspend fun fetchHotNews(
        selectedTitle: String
    ): BaseRespond<NewsNetworkEntity> {

        val request = NewsAPIRequest(selectedTitle)

        return WebApiRequestHandler()
                .requestRestApiAsync(
                        apiService
                        , app
                        , serverPublicCertificate
                        , tnCrypto
                        , request
                        , HEAD_LINES
                )
    }
}