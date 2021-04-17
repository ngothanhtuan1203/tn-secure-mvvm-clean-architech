package com

import com.crypto.di.TNCryptoInject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CryptoUnitTest {

    private val crypto = TNCryptoInject.provideTNCrypto()
    private val SERVER_PUB_CERT : String =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgEFF5yaQFOQNLq7npN7yAaXTKsUFUq8i0F0K1kcS4k/38k8SHhFhFhzLVlGelHoIRGyfWE9Xovgvz7XfeIwxsvg4wroQUGUA3jlfIxyDNHFKLgiQXnfyLFptF9O3Z2PhJkZ8exh6yq85g24tpSEfq7g0JipmUf/2vUxL56TfUiA7f8Z6cQdQ/WXFMCvtonQeMKKOATMfRoHirZx7x75Dn1K7EonBtMDdIKriu4RbkWBGhMB8QgNxGZ2IQQI7k6i1IvH4/t9qhS+0e9+0nPPtSTBwp1CWo2wCXUYF4v3pq9NtlsJmzL/NHsTEOg+zl3KEmUfHLVrf70QF5nHsIzdgbQIDAQAB"
    val aesRandomKey = crypto.randomBytes(32)

    @Test
    fun testInitOnesecslot() {
        val onsecSlot = crypto.initOnceSecSlot()
        assertNotNull(onsecSlot)
    }

    @Test
    fun testGetPublicKeyFromPem() {
        val publicKey = crypto.getPublicKeyFromPem(SERVER_PUB_CERT)
        assertNotNull(publicKey)
    }

    @Test
    fun testGetPrivateKey() {
        val privateKey = crypto.getPrivateKey()
        assertNotNull(privateKey)
    }

    @Test
    fun testInitSessionKey() {
        val publicKey = crypto.getPublicKeyFromPem(SERVER_PUB_CERT)
        val sessionkey = crypto.initSessionKey(publicKey, aesRandomKey)

        assertNotNull(sessionkey)
    }

    @Test
    fun testEncrypDecryptFlow() {
        val tobeEncryptedData = " This is the data to be encrypted,have  some with it"
        val encryptedData = crypto.aesEncrypt(tobeEncryptedData, aesRandomKey)
        val decryptedData = crypto.aesDecrypt(encryptedData, aesRandomKey)
        assertEquals(tobeEncryptedData, decryptedData)
    }


}