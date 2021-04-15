package com.data.remote

import com.data.remote.entity.request.SecureRequest
import com.data.remote.entity.respond.SecureRespond
import com.data.util.API_PATH
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path


interface APIService {
    @POST("$API_PATH{endpoint}")
    suspend fun postAsyncApi(
        @Body requestBody: SecureRequest,
        @Path("endpoint") endPoint: String
    ): SecureRespond
}