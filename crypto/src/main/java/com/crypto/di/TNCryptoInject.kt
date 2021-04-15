package com.crypto.di

import com.crypto.TNAESHelper
import com.crypto.TNRSAHelper
import com.crypto.TNCrypto
import com.crypto.TNSecureRandom

object TNCryptoInject {
    /**
     * internal methods
     */
    private val tnAESHelper: TNAESHelper =
        TNAESHelper()
    private val tnRSAHelper: TNRSAHelper =
        TNRSAHelper()
    private val secureRandom: TNSecureRandom = TNSecureRandom()

    private val tnCrypto: TNCrypto =
        TNCrypto()

    internal fun provideRSAHelper(): TNRSAHelper {
        return tnRSAHelper
    }
    internal fun provideAESHelper(): TNAESHelper {
        return tnAESHelper
    }

    internal fun provideTNSecureRandom(): TNSecureRandom {
        return secureRandom
    }

    /**
     * Public methods
     */
    fun provideTNCrypto(): TNCrypto {
        return tnCrypto
    }
}