package com.data.remote

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Base64
import com.crypto.TNCrypto
import com.data.remote.entity.request.BaseRequest
import com.data.remote.entity.request.SecureRequest
import com.data.remote.entity.respond.BaseRespond
import com.data.util.JsonUtil
import com.tnsecure.logs.TNLog
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class WebApiRequestHandler {
    val DATA = "res/raw/bin.dat"
    suspend inline fun <reified T> requestRestApiAsync(
        apiService: APIService,
        app: Application,
        serverCertificate: String,
        tnCrypto: TNCrypto,
        request: BaseRequest,
        endPoint: String
    ): BaseRespond<T> {

       if (!isNetworkConnected(app)) {
           return BaseRespond(null, "Your device not connected to internet", false)
       }
        /**
         * Create a random AES key.
         */
        val sessionKey = tnCrypto.randomBytes(32)

        /**
         * Make wrapper secure request
         */
        val secureRequest = wrapSecureRequest(request, tnCrypto, sessionKey, serverCertificate)
        return try {
            val respondContainer =
                apiService.postAsyncApi(secureRequest, endPoint)

            if (respondContainer.isSuccess()) {
                val tobeSign =
                    secureRequest.nonce + respondContainer.code + respondContainer.data


                val isVerified = tnCrypto.verifySignedData(
                    tobeSign.toByteArray(),
                    Base64.decode(respondContainer.signature, Base64.NO_WRAP),
                    tnCrypto.getPublicKeyFromPem(serverCertificate)!!
                )

                return if (isVerified) {

                    val encryptedRespond = respondContainer.data
                    val plainTextRespond = tnCrypto.aesDecrypt(
                        encryptedRespond,
                        sessionKey
                    )
                    TNLog.d("WebApiRequestHandler","plainTextRespond:"+plainTextRespond)
                    val respondData: T? = parseRespond(plainTextRespond)
                    BaseRespond(respondData, "Success", true)

                } else {
                    BaseRespond(null, "Wrong signature", false)
                }
            } else {
                BaseRespond(null, respondContainer.message, false)
            }
        } catch (e: Exception) {
            BaseRespond(null, e.localizedMessage, false)
        }
    }

    inline fun <reified T> parseRespond(data: String): T? {
        return JsonUtil.fromJsonStringToObj(
            data,
            T::class.java
        )
    }

    inline fun wrapSecureRequest(
        baseRequest: BaseRequest,
        tnCrypto: TNCrypto,
        sessionKey: ByteArray,
        serverCertificate: String
    ): SecureRequest {
        /**
         * Key use internal app.
         */
        val encryptedBytes = readFileResource(javaClass, DATA)
        val privateKey = tnCrypto.getPrivateKey(tnCrypto.getSecretKey(), encryptedBytes)
        val publicKey = tnCrypto.getPublicKeyFromPem(serverCertificate)

        /**
         * Session key: 32bit random key use in every session
         */
        val sessionENCKey = tnCrypto.initSessionKey(publicKey, sessionKey)

        val onesecslot = tnCrypto.initOnceSecSlot()

        val requestContainer = JsonUtil.toJsonString(baseRequest);

        val requestData = tnCrypto.aesEncrypt(
                requestContainer,
                sessionKey
        )
        val nonce = tnCrypto.nextKey(16)
        val tobeSign = (sessionENCKey + onesecslot + requestData + nonce)
        val signature =
            tnCrypto.rsaSign(privateKey, tobeSign)


        return SecureRequest(
                requestData,
                sessionENCKey,
                onesecslot,
                nonce,
                signature
        )
    }

    fun isNetworkConnected(context:Application): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    @Throws(IOException::class)
     inline fun readFileResource(
        resourceClass: Class<*>,
        resourcePath: String
    ): ByteArray {
        val `is` =
            resourceClass.classLoader?.getResourceAsStream(resourcePath)
                ?: throw IOException("cannot find resource: $resourcePath")
        return getBytesFromInputStream(`is`)
    }

    @Throws(IOException::class)
    inline fun getBytesFromInputStream(`is`: InputStream): ByteArray {
        val os = ByteArrayOutputStream()
        val buffer = ByteArray(8192)
        var len: Int
        while (`is`.read(buffer).also { len = it } != -1) {
            os.write(buffer, 0, len)
        }
        os.flush()
        return os.toByteArray()
    }
}