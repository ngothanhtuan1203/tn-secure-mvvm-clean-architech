package com.data.di

import android.content.Context
import com.crypto.TNCrypto
import com.crypto.di.TNCryptoInject
import com.data.local.LocalDataSource
import com.data.local.LocalDataSourceImpl
import com.data.local.room.AppDatabase
import com.data.remote.APIService
import com.data.remote.RemoteDataSource
import com.data.remote.RemoteDataSourceImpl
import com.data.util.*
import com.tnsecure.logs.TNLog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataModule {
    private fun interceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
    private val SERVER_PUB_CERT : String =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgEFF5yaQFOQNLq7npN7yAaXTKsUFUq8i0F0K1kcS4k/38k8SHhFhFhzLVlGelHoIRGyfWE9Xovgvz7XfeIwxsvg4wroQUGUA3jlfIxyDNHFKLgiQXnfyLFptF9O3Z2PhJkZ8exh6yq85g24tpSEfq7g0JipmUf/2vUxL56TfUiA7f8Z6cQdQ/WXFMCvtonQeMKKOATMfRoHirZx7x75Dn1K7EonBtMDdIKriu4RbkWBGhMB8QgNxGZ2IQQI7k6i1IvH4/t9qhS+0e9+0nPPtSTBwp1CWo2wCXUYF4v3pq9NtlsJmzL/NHsTEOg+zl3KEmUfHLVrf70QF5nHsIzdgbQIDAQAB"

    /**
     * Remote data source
     */
    @Provides
    @BaseURL
    fun provideBaseUrl() = SERVER_URL

    @Provides
    @ServerPublicCertificate
    fun provideServerPublicCertificate() = SERVER_PUB_CERT

    @Provides
    @APIKey
    fun provideAPIKey() = API_KEY

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BaseURL

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ServerPublicCertificate

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class APIKey

    @Provides
    @Singleton
    fun provideOkHttpClient(@DataModule.BaseURL BASE_URL: String): OkHttpClient {
        val sslSig =
            if (SSL_SHA.contains("sha256")) SSL_SHA else "sha256/" + SSL_SHA
        TNLog.d("ApiClient", "SSL_SHA:$sslSig")
        val certificatePinner = CertificatePinner.Builder()
            .add(BASE_URL.replace("https://", ""), sslSig)
            .build()


        val httpClient: OkHttpClient.Builder =
            OkHttpClient.Builder().certificatePinner(certificatePinner)
        httpClient.addInterceptor(interceptor())
        httpClient.readTimeout(TIME_OUT, TimeUnit.SECONDS)
        httpClient.writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @DataModule.BaseURL BASE_URL: String
    ): Retrofit {
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        return builder.client(okHttpClient).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): APIService = retrofit.create(
        APIService::class.java
    )


    @Provides
    @Singleton
    fun provideRemoteDataSource(remoteDataSource: RemoteDataSourceImpl): RemoteDataSource =
            remoteDataSource

    @Provides
    @Singleton
    fun provideTNCrypto(): TNCrypto =
            TNCryptoInject.provideTNCrypto()

    /**
     * Local data source
     */

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideNoteDao(db: AppDatabase) = db.noteDao()

    @Singleton
    @Provides
    fun provideLocalDataSource(localDataSource: LocalDataSourceImpl): LocalDataSource = localDataSource

}